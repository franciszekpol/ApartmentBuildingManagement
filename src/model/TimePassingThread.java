package model;

import service.LeaseService;

public class TimePassingThread extends Thread{
    private volatile boolean suspended = false;
    private LeaseService leaseService;
    private Integer currentDay = 1;

    public TimePassingThread(LeaseService leaseService) {
       this.leaseService = leaseService;
    }

    public void run() {
        while(true) {
            processAllLeaseEveryTwoDays();
            try {
                synchronized (this) {
                    // Na potrzeby testowania 2 sekundy, docelowo według polecenia ma być 5 sekund
                    this.sleep(2000);
                    while(suspended) {
                        this.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("============Dzień " + currentDay + "============");
            currentDay++;
        }
    }

    private void processAllLeaseEveryTwoDays() {
        if(currentDay %2 == 0) {
            leaseService.processAllLease(currentDay);
        }
    }

    public void suspendThread() {
        suspended = true;
    }

    public void resumeThread() {
        suspended = false;
        synchronized (this) {
            notify();
        }
    }
}
