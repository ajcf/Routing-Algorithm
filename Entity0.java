public class Entity0 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity0()
    {
      //This for loop initializes all of the costs to infinity, because no values are known.
      for(int f = 0; f < 4; f++){
        for(int g = 0; g < 4; g++){
          distanceTable[f][g] = 999;
        }
      }
      //we can now plug in the costs that are given in the diagram
      distanceTable[0][0] = 0;
      distanceTable[1][1] = 1;
      distanceTable[2][2] = 2;
      distanceTable[3][3] = 7;
      //This creates a new array which we will use to hold Entity0's min costs to each other node,
      //and eventually send that data using toLayer2().
      int[] minDistance = new int[4];
      //This for loop actually calculates the minimum cost to each node.
      for(int h = 0; h < 4; h++){
        //int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][1], b);
      }
      //This for loop sends the packet containing cost data to each other node.  It starts at 1 to avoid
      //sending the packet to Entity0.
      for(int i = 1; i < 4; i++){
        //this line builds the packet with destination 1-3
        Packet dtPacket = new Packet(0, i, minDistance);
        //this line sends it.
        NetworkSimulator.toLayer2(dtPacket);
      }
      // Prints the time, the fact that the initialization has completed, and the current Distance Table.
      System.out.println("Entity0 Initializion Complete. Distance Table is:");
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
      //first, we define a boolean to mark whether any minDistance has changed. If none have changed,
      //we won't send out a packet.  It's initialized to false, because nothing has changed yet.
      boolean send = false;
      //Again, an array to hold and eventually send minimum distance values
      int[] minDistance = new int[4];
      //This for loop recalculates the min costs to each node and again store them in minDistance.
      for(int h = 0; h < 4; h++){
        //int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][1], b);
      }
      //This for loop checks to see if the cost in the distance table of getting to each node via p's source,
      // is less than the cost found by calculating the Mincost given by p plus the cost in the distance table 
      //of getting to each node. 
      for(int k = 0; k<4; k++){
        if(p.getMincost(k)+minDistance[p.getSource()] < distanceTable[k][p.getSource()]){
          //if so, it changes the value in the distance table to reflect the lesser value
          distanceTable[k][p.getSource()] = p.getMincost(k)+minDistance[p.getSource()];
          //it also checks to see if this is the new minimum distance for reaching this node.  If this
          //minimum value has changed, it changes send to true.
          if(distanceTable[k][p.getSource()]<minDistance[k]){
            minDistance[k] = distanceTable[k][p.getSource()];
            send = true;
          }
        }
      }
      //if the minimum value has changed, it sends update packets to each other node.
      if(send){
        for(int i = 1; i < 4; i++){
          Packet dtPacket = new Packet(0, i, minDistance);
          NetworkSimulator.toLayer2(dtPacket);
        }
      }
      //Prints the time, the fac that the update method just completed, and the current Distance Table.
      System.out.println("Entity0 Update Complete. Distance Table is:");
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
      distanceTable[0][0] = 0;
      distanceTable[1][1] = 1;
      distanceTable[2][2] = 2;
      distanceTable[3][3] = 7;
      //next, we replace the value of getting to the node with the changed link with the new cost
      distanceTable[whichLink][whichLink] = newCost;
      //Once again, we initialize and calculate the minimum distance array.
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(distanceTable[h][1], b);
      }
      //finally, we send packets out to each node with the new costs.
      for(int i = 1; i < 4; i++){
        Packet dtPacket = new Packet(0, i, minDistance);
        NetworkSimulator.toLayer2(dtPacket);
      }
      System.out.println("Entity0 linkCostChange Handled. Distance Table is:");
      System.out.println("Time now is " + NetworkSimulator.time + ".");
      printDT();
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2   3");
        System.out.println("----+------------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
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
