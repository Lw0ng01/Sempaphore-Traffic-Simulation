// This file defines class "Car".

// This code uses
//      class Synch, which defines the semaphores and variables
//                   needed for synchronizing the cars.

public class Car extends Thread {
  int myName;  // The variable myName stores the name of this thread.
               // It is initialized in the constructor.



  // This is the constructor for class Car.  It has an integer parameter,
  // which is the name that is given to this thread.
  public Car(int name) {
    // copy the parameter value to local variable "MyName"
    myName = name;

    // Call threadStart to let the timeSim scheduler know that another
    // thread is starting.  timeSim needs to know how many threads there
    // are, so that it can accurately judge when all threads have finished
    // their current computation, so that simulated time can be advanced.
    Synch.timeSim.threadStart();
  }  // end of the constructor for class "Car"



  public void run () {
    for (int I=1;  I<= 4; I++) {

      // Simulate driving around Barriefield.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is driving around Barriefield.\n");
      Synch.timeSim.doSleep(1, 500);

      // Now cross the causeway westbound, into Kingston.  This might
      // involve some waiting (if the westbound light is red).
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " wants to cross westbound.\n");

      // *** Put synchronization code here, to make cars wait if the westbound
      // *** light is red.
          //     Note that there should be some reaction time, so when
	  //     the light turns green the first car starts crossing.  Then after a
	  //     short pause (simulated, for example, by "sleep(1)"), the second car
	  //     starts crossing, and so on.  This is more realistic as
	  //     when you are waiting in a long line of cars at a traffic light, you see the
	  //     light turn green up there, but it takes a while before all the cars ahead
	  //     of you get going, so you have to keep waiting even though the light is green.
	  //     In some cases, you don't even make it across on this green; you have to 
	  //     wait through another red cycle for the next green.
	  
      Synch.mutex.acquire(); // acquire mutex for west count and traffic light

      // if light is red on westbound or there are cars waiting 
      if ((TrafficLight.west_bound_red == true) || (Synch.west_count > 0))
      {
        System.out.println("Car " + myName + " waits at Westbound light at time " + Synch.timeSim.curTime() + "\n");
        Synch.west_count++; // increment cars at stop

        Synch.mutex.release();
        Synch.west_entrance.acquire(); // acquire west_entrance since it needs to block westbound
      }
      else
      {
        Synch.mutex.release();
      }

      // Now we have permission to cross the causeway.  Crossing is simulated
      // by a sleep.  The sleep time is chosen to be relatively long (compared
      // to the sleep times for driving around and getting donuts), so that
      // it frequently happens that several cars are on the bridge.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is starting to cross westbound.\n");
      Synch.timeSim.doSleep(100);

      // Simulate driving to petrol station, filling the car fuel tank with gasoil/diesel, leaving petrol station, and
      // driving back to the causeway.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is filling the car fuel tank at the petrol station.\n");
      Synch.timeSim.doSleep(1, 500);

      // Now cross the causeway eastbound, back into Barriefield.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " wants to cross eastbound.\n");

      // *** Put synchronization code here, to make cars wait if the eastbound
      // *** light is red..

      Synch.mutex.acquire(); // acquire mutex for traffic light and east count

      // if traffic light for eastbound is red, or there are cars waiting on east end
      if ((TrafficLight.east_bound_red == true) || (Synch.east_count > 0))
      {
        System.out.println("Car " + myName + " waits at Eastbound light at time " + Synch.timeSim.curTime() + "\n");
        Synch.east_count++; // increment cars at east stop

        Synch.mutex.release();
        Synch.east_entrance.acquire(); // acquire east_entrance since it needs to block for eastbound
      }
      else
      {
        Synch.mutex.release();
      }

      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is starting to cross eastbound.\n");
      Synch.timeSim.doSleep(100);

    } // end of "for" loop
    System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " has stoped and its driver went out.\n");
    Synch.timeSim.threadEnd();  // Let timeSim know that this thread
                                // has ended.
  }  // end of "run" method
}  // end of class "Car"

