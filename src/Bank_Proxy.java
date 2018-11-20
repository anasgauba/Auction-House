public class Bank_Proxy extends Thread {

    private boolean run;


    public Bank_Proxy() {


        this.run = true;
        start();
    }

    @Override
    public void run() {


        while (run) {


            //System.out.println("Bank Proxy Alive!");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }



}
