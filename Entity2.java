public class Entity2 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity2()
    {
      for(int f = 0; f < 4; f++){
        for(int g = 0; g < 4; g++){
          distanceTable[f][g] = 999;
        }
      }
      //this is one of thep places where Entity1 differs.  The distance table is being initialized with values
      //pertaining to Entity2, not Entity0.
      distanceTable[0][0] = 3;
      distanceTable[1][1] = 1;
      distanceTable[2][2] = 0;
      distanceTable[3][3] = 2;
     
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        minDistance[h] = Math.min(a, distanceTable[h][3]);
      }
      //This is another place where Entity2 differs.  As you can see, my for loop loops through all four entities,
      //but checks to make sure that it doesn't actually send a packet to itself using an if.
      //Also, the from field when making the packet is 2.
      for(int i = 0; i < 4; i++){
        if(i != 2){
          Packet dtPacket = new Packet(2, i, minDistance);
          NetworkSimulator.toLayer2(dtPacket);
        }
      }
      System.out.println("Entity2 Initializion Complete. Distance Table is:");
      System.out.println("Time now is " + NetworkSimulator.time + ".");
      printDT();
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {
      boolean send = false;
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(a, b);
      }
      for(int k = 0; k<4; k++){
        if(p.getMincost(k)+minDistance[p.getSource()] < distanceTable[k][p.getSource()]){
          distanceTable[k][p.getSource()] = p.getMincost(k)+minDistance[p.getSource()];
          if(distanceTable[k][p.getSource()]<minDistance[k]){
            minDistance[k] = distanceTable[k][p.getSource()];
            send = true;
          }
        }
      }
      //again, the sending protocol is different.
      if(send){
        for(int i = 0; i < 4; i++){
          if(i != 2){
            Packet dtPacket = new Packet(2, i, minDistance);
            NetworkSimulator.toLayer2(dtPacket);
          }
        }
      }
      System.out.println("Entity2 Update Complete. Distance Table is:");
      System.out.println("Time now is " + NetworkSimulator.time + ".");
      printDT();
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
      //we start out by deleting all previous data, becuase it could be wrong.
      for(int f = 0; f < 4; f++){
        for(int g = 0; g < 4; g++){
          distanceTable[f][g] = 999;
        }
      }
      //we can now plug in the costs that are given in the diagram
      distanceTable[0][0] = 3;
      distanceTable[1][1] = 1;
      distanceTable[2][2] = 0;
      distanceTable[3][3] = 2;
      //next, we replace the value of getting to the node with the changed link with the new cost
      distanceTable[whichLink][whichLink] = newCost;
      //Once again, we initialize and calculate the minimum distance array.
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        //int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(a, distanceTable[h][3]);
      }
      //finally, we send packets out to each node with the new costs.
      for(int i = 0; i < 4; i++){
        if(i != 2){
          Packet dtPacket = new Packet(2, i, minDistance);
          NetworkSimulator.toLayer2(dtPacket);
        }
      }
      System.out.println("Entity2 linkCostChange Handled. Distance Table is:");
      System.out.println("Time now is " + NetworkSimulator.time + ".");
      printDT();
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D2 |   0   1   3");
        System.out.println("----+------------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 2)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (j == 2)
                {
                    continue;
                }
                
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
