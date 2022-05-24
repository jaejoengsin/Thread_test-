package ThreadTest;
import java.lang.Thread;
import java.util.Scanner;

public class ThreadTest {
	private static final long sleeptime = 1000;
	private static int count = 0;
	private static int num = 10;
	private String state = "state";
	private final Object ob = new Object();
    private Scanner sc = new Scanner(System.in);


	// ob를 하든, this를 하든 결국 두 스레드가 똑같은 메서드에 접근했을때 먼저 접근한 스레드가 lock을 하기때문에 뒤에 들어온 스레드는
	// 기다릴수밖에 없음
	public void Case1print(String s) {
		synchronized (ob) {
			try {
				count += 1;
				System.out.println("test스레드에 " + s + "접근" + ", 횟수:" + count);
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

	public void Case2print1(String s) {
		synchronized (this) {
			state = s;
			System.out.println(state);
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
	
				e.printStackTrace();
			}
		}
	}
	
	public void Case2print2(String s) {
		synchronized (this) {
			state = s;
			System.out.println(state);
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
	
	public synchronized void printnum(String s, int n){
		if(num>0){
		num+=n;
		System.out.println(s+"and num: "+num);
	}
	}

	

	public void Case1(ThreadTest test1) {
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				test1.Case1print("Thread1");
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				test1.Case1print("Thread2");
			}
		}).start();

	}


	//이름만 다르고 내용이 같은 메서드 2개에  스레드가 각각 접근하여도 동일한 객체에 대해 lock을 걸기 때문에 
	//먼저 접근한 스레드가 먼저 실행될 수밖에 없다. 만약 서로 다른 객체에 대해 lock을 건다면 두 스레드는 서로 영향을 주지 않고 각자의 일을 할
	//것이다. 그러나 이 메서드에서는 state라는 동일한 값에 접근하기 때문에 그렇게 변경을 하면 스레드의 충돌이 일어날 것이다. 
	public void Case2(ThreadTest test1) {
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				test1.Case2print1("Thread1");
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				test1.Case2print2("Thread2");
			}
		}).start();

	}
	//static 변수 num이 0보다 작아질 때까지 두 스레드가 printnum함수를 호출하여 num에 각각 +1,-2를 함.
	public void Case3(ThreadTest test1) {
		new Thread(() -> {
			while(num>0){
				printnum("Thread1", 1);
			}
		}).start();

		new Thread(() -> {
			while(num>0){
				printnum("Thread2", -2);
			}
		}).start();
	}

	public static void main(String[] args) {
		ThreadTest test1 = new ThreadTest();
        System.out.print("choice test(Case 1,2,3) >>");
        int choice = test1.sc.nextInt();
        switch(choice){
            case 1:
            System.out.println("Test Case1 start!");
            test1.Case1(test1);//두 스레드가 synchronized 블록을 가진 동일한 메서드에 접근했을 때
            break;
            case 2:
            System.out.println("Test Case2 start!");
            test1.Case2(test1);//두 스레드가 synchronized 블록을 가진 서로 다른 두개의 메서드에 접근했을 때 
            break;
            case 3:
            System.out.println("Test Case3 start!");
            test1.Case3(test1);//두 스레드가 동일한 synchronized 메서드에 접근했을 때
            break;
        }
    }

	
}

