public class TrafficLight extends Thread {

    public static boolean east_bound_red = true;
    public static boolean west_bound_red = true;
    public static int green_light = 420;
    public static int car = 2;
    

    public void run()
    {
        int T;
        int Q = 0;
        Synch.timeSim.threadStart();
        int remaining_time;

        while(Q < 100)
        {
            Synch.mutex.acquire(); // acquire mutex for trafficlight

            // east light turns green
            east_bound_red = false;
            System.out.println("The Westbound light is red at time " + Synch.timeSim.curTime());
            System.out.println("\nThe Eastbound lgiht is now green at time: " + Synch.timeSim.curTime());

            remaining_time = green_light;

            while ((Synch.east_count > 0) && (remaining_time > 0))
            {
                Synch.east_entrance.release();
                Synch.mutex.release();
                Synch.timeSim.doSleep(car); // time gap between cars

                Synch.mutex.acquire(); // acquire mutex for car count
                --Synch.east_count;
                remaining_time = remaining_time - car;
            }

            Synch.mutex.release();

            Synch.timeSim.doSleep(remaining_time);

            Synch.mutex.acquire(); // acquire traffic light mutex

            // light is now red for both directions
            east_bound_red = true;
            west_bound_red = true;

            System.out.println("The Eastbound light is now red at time: " + Synch.timeSim.curTime());
            System.out.println("\nThe Westbound light is now red at time: " + Synch.timeSim.curTime());

            Synch.mutex.release(); // release traffic light mutex
            Synch.timeSim.doSleep(100);

            Synch.mutex.acquire(); // acquire mutex for trafficlight on westbound

            // west light is now green
            west_bound_red = false;
            System.out.println("The Westbound light is green at time " + Synch.timeSim.curTime());
            System.out.println("\nThe Eastbound lgiht is now red at time: " + Synch.timeSim.curTime());

            remaining_time = green_light;

            while((Synch.west_count > 0) && (remaining_time > 0))
            {
                Synch.west_entrance.release(); // release mutex for traffic light and count
                Synch.mutex.release();

                Synch.timeSim.doSleep(car); // time gap between cars

                Synch.mutex.acquire(); // acquire westbound car count
                --Synch.west_count;
                remaining_time = remaining_time - car;
            }

            Synch.mutex.release();
            
            Synch.timeSim.doSleep(remaining_time);

            Synch.mutex.acquire();

            // both lights are now red
            east_bound_red = true;
            west_bound_red = true;

            System.out.println("The Eastbound light is now red at time: " + Synch.timeSim.curTime());
            System.out.println("\nThe Westbound light is now red at time: " + Synch.timeSim.curTime());

            Synch.mutex.release();
            Synch.timeSim.doSleep(100);

            ++Q;


        }
    }

}
