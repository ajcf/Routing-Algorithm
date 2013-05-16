public class Entity1 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity1()
    {
      for(int f = 0; f < 4; f++){
        for(int g = 0; g < 4; g++){
          distanceTable[f][g] = 999;
        }
      }
      //This section has the same function as in Entity0, but the values are Entity1's.  As you can see,
      //Entity1 does not connected to Entity3, so the cost between them is infinite.
      distanceTable[0][0] = 1;
      distanceTable[1][1] = 0;
      distanceTable[2][2] = 1;
      distanceTable[3][3] = 999;
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        //int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][0], b);
      }
      //This send protocol differs from Entity0's, because Entity1 only sends 2 packets, and I decided that it would
      //be more efficient if I just made each packet than if I were to run a for loop.
      Packet dtPacket = new Packet(1, 0, minDistance);
      NetworkSimulator.toLayer2(dtPacket);
      dtPacket = new Packet(1, 2, minDistance);
      NetworkSimulator.toLayer2(dtPacket);
      System.out.println("Entity1 Initializion Complete. Distance Table is:");
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
        //int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][0], b);
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
      //this send protocol is the same as the one in the construstor.  It once again differs from Entity0's.
      if(send){
        Packet dtPacket = new Packet(1, 0, minDistance);
        NetworkSimulator.toLayer2(dtPacket);
        dtPacket = new Packet(1, 2, minDistance);
        NetworkSimulator.toLayer2(dtPacket);
      }
      System.out.println("Entity1 Update Complete. Distance Table is:");
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
      distanceTable[0][0] = 1;
      distanceTable[1][1] = 0;
      distanceTable[2][2] = 1;
      distanceTable[3][3] = 999;
      //next, we replace the value of getting to the node with the changed link with the new cost
      distanceTable[whichLink][whichLink] = newCost;
      //Once again, we initialize and calculate the minimum distance array.
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][0], b);
      }
      //finally, we send packets out to each node with the new costs.
      Packet dtPacket = new Packet(1, 0, minDistance);
      NetworkSimulator.toLayer2(dtPacket);
      dtPacket = new Packet(1, 2, minDistance);
      NetworkSimulator.toLayer2(dtPacket);
      System.out.println("Entity1 linkCostChange Handled. Distance Table is:");
      System.out.println("Time now is " + NetworkSimulator.time + ".");
      printDT();
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("         via");
        System.out.println(" D1 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 1)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 2)
            {
            
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
