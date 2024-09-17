package posy_system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Post_main {
	static Scanner sc = new Scanner(System.in);
	private static Map<String, Integer> menu = new HashMap<String, Integer>();

	public static void init(){//Pos�⸦ Ű�� �ʱ�ȭ�� �Ѵ�. menu.txt���� �޴��� �Է¹޾� �����ϰ� ������� 0���� �ʱ�ȭ��Ų��.
		int count = 0;
		String tmp1 = "";//�޴�
		int tmp2;//����
		try{
			File file = new File("./menu.txt");
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while((line = bufReader.readLine()) != null){
				if(count %2 == 0){
					tmp1 = line;
				}else{
					tmp2 = Integer.parseInt(line);
					menu.put(tmp1, tmp2);
				}
				++count;
			}
			bufReader.close();
		}catch (FileNotFoundException e) {
			// TODO: handle exception
		}catch(IOException e){
			System.out.println(e);
		}
		System.out.println("�ý��� ���� �� �ʱ�ȭ �Ϸ�\n");
		String init_log = "";
		init_log = "(init)�ý��� ���� �� �ʱ�ȭ �Ϸ�\n";
		System_access.admin_log(init_log);
	}
	//�ʱ⼳��

	public static void select_menu(Customer[] people){
		String hm;
		int num = 0;
		int price = 0;
		String total_sale_log = "";
		while(true){
			//��� �մ����� Ȯ���ؾ���
			System.out.println("�޴�����\norder   calculate   add_menu   sub_menu   total_sale   menu_find   exit");
			hm = sc.nextLine();
			System.out.println();
			if(hm.equals("order")){
				System.out.println("����մ�(0~9)");
				num = sc.nextInt();
				sc.nextLine();
				if(people[num].get_sales() == 0){//������ sales�� 0�ϰ�� ù �ֹ����� �Ǵ��Ͽ� �� ��ü ���� �� ī��Ʈ�ٿ� ����
					people[num] = new Customer();
					people[num].set_table(num);
					Sale.order(people[num], menu);
					people[num].countdown();//order�� ù �ֹ��̹Ƿ� ù �ֹ��� ���ÿ� ī��Ʈ�ٿ��� �̷�����
				}else{
					Sale.order(people[num], menu);
				}
			}else if(hm.equals("calculate")){
				System.out.println("����մ�(0~9)");
				num = sc.nextInt();
				sc.nextLine();
				System.out.print("������ ������ �ݾ� : ");
				price = sc.nextInt();
				sc.nextLine();
				Sale.calculate(people[num], price);//sales�� Ŭ���� ��ü�� �ٲ����  ����.sales��
				people[num].set_count_minute(-1);//����� �Ŀ� ���� ��ü�� count_minute�� 0���� ����� �̿�ð��� ����ǵ��� �Ѵ�.
				//��ü���ŵ� �ؾ��Ұ� ������
			}else if(hm.equals("add_menu")){
				menu_access.add_menu(menu);
			}else if(hm.equals("sub_menu")){
				menu_access.sub_menu(menu);
			}else if(hm.equals("menu_find")){
				menu_access.menu_find(menu);
			}else if(hm.equals("total_sale")){
				System.out.println("�� ���� : " + Sale.total_sales + "��");
				total_sale_log = "(total_sale)�� ���� : " + Sale.total_sales + "��\n";
				System_access.admin_log(total_sale_log);
			}else if(hm.equals("exit")){
				break;
			}else{
				System.out.println("��ŸȮ�� �ٽ� �Է����ֽʽÿ�");
			}
		}
		System.out.println("�ý����� ����Ǿ����ϴ�.");
		String logout = "";
		logout = "�ý����� ����Ǿ����ϴ�.\n";
		System_access.admin_log(logout);
	}


	public static void main(String[] args){
		init();
		Customer[] people = new Customer[10];

		for(int i = 0; i<10; i++){
			people[i] = new Customer();
			people[i].set_table(i);
		}//��ü�� �ʱ�ȭ - �ʱ�ȭ ���ؼ� ������Ʈ ���� ������ �ű��ϳ�

		select_menu(people);
	}
}

class Customer{
	private int sales;
	private int count_minute;
	private int table_set;

	public Customer(){
		sales = 0;
		count_minute = 40;
		table_set = 0;
	}
	public int get_sales(){
		return sales;
	}
	public void set_sales(int sale){
		sales = sale;
	}
	public int get_table(){
		return table_set;
	}
	public void set_table(int table){
		table_set = table;
	}
	public int get_count_minute(){
		return count_minute;
	}
	public void set_count_minute(int count_minute){
		this.count_minute = count_minute;
	}

	//������ ī��Ʈ�� ����Ǿ���ϴµ� m_timer.cancel()�Ұ�� �� ������ �׷��� Ÿ�̸� �迭�� ã�� Ÿ�̸� 10���� ���� ���� ���������ϴ� ��Ȳ
	public void countdown(){
		Timer m_timer[] = new Timer[10];
		for(int i = 0; i < 10; i++){
			m_timer[i] = new Timer();
		}
		TimerTask m_task = new TimerTask(){
			public void run(){//���߿� ���� Ŭ�������� count_minute���ڷ� �޾Ƽ� ����ҵ�...?
				if(count_minute >= 0){
					if(count_minute % 10 ==0 && count_minute != 0){//���ʸ��� �ʱ�ȭ ������ ǥ�ô� 10�ʿ� �ѹ��� ����, �׸��� calculate�� ��� �̿�ð� 0�� �ٷ� ���ŵ� �� �ֵ���
						System.out.println(table_set +"�� ������ �̿�ð���"+count_minute + "�� ���ҽ��ϴ�.");
					}
					count_minute = count_minute-1;
					if(count_minute == 0){
						System.out.println(table_set +"�� ������ �̿�ð��� ����Ǿ����ϴ�.");
					}
				}else{
					m_timer[table_set].cancel();
				}
			}
		};
		m_timer[table_set].schedule(m_task,0,1000);
	}
}


class Sale {
	static int total_sales;
	static String order_menu;
	static Scanner sc = new Scanner(System.in);

	public Sale(){
		total_sales = 0;
	}

	public static void order(Customer people, Map<String, Integer> menu){
		String order_log = "";
		while(true){
			System.out.println("������ �ֹ��Ͻðڽ��ϱ�");
			order_menu = sc.nextLine();

			if(order_menu.equals("exit")){
				break;
			}else if(menu.containsKey(order_menu) == false){
				System.out.println("���� �޴��Դϴ� �ٽ� �õ��Ͻʽÿ�\n");
				continue;
			}else{
				System_access.output(menu, order_menu);
				people.set_sales(people.get_sales() + menu.get(order_menu));
				order_log = "(order)"+people.get_table() + "�� ������ " + order_menu + "�� �ֹ��Ͽ����ϴ�.\n";
				System_access.admin_log(order_log);
			}
		}
		System.out.println("�� �ݾ� : " + people.get_sales() +"��\n");//sales�� ���� ��ü���� 1����
		//ī��Ʈ�ٿ� �־���մϴ�. countdown(people); ��ü�� ���� ��
	}
	//ó���ֹ�
/*
	public static void countdown(){//���� Ŭ������ ���ڷ� �޾ƾ���
		m_timer.schedule(m_task,0,1000);
	}
	//ó���ֹ� �� �ð� �귯������
*/


	public static void calculate(Customer people, int money){
		if(people.get_sales() > money){
			System.out.println("�ݾ��� ���ڶ��ϴ�.");//���� ���ڶ����� �����ؾ���
		}else{
			System.out.println("�������� " + money + "���� �ް� " + (money-people.get_sales()) + "���� �Ž����־����ϴ�.");
		}
		total_sales = total_sales + people.get_sales();

		String calculate_log = "";
		calculate_log = "(calculate)�������� " + money + "���� �ް� " + (money-people.get_sales()) + "���� �Ž����־����ϴ�.\n";
		System_access.admin_log(calculate_log);
		//����ϸ� ������ sales�� 0���� �ʱ�ȭ���ְ�, countdown�� 120���� �ʱ�ȭ���ش�.
	}
	//���


}


class System_access{
	public static void output(Map<String, Integer> menu, String a){
		System.out.println("�����Բ��� " + a + "�� " + menu.get(a) + "���� �ֹ��ϼ̽��ϴ�.\n");
	}

	public static void admin_log(String a){
		File file = new File("./admin_log.txt");
		FileWriter writer = null;
		try{
			writer = new FileWriter(file, true);
			writer.write(a);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(writer != null){
					writer.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}

class menu_access{
	static String add_menu;
	static String sub_menu;
	static int menu_price;
	static Scanner sc = new Scanner(System.in);

	public static void add_menu(Map<String, Integer> menu){
		System.out.println("� �޴��� �߰��Ͻðڽ��ϱ�?");
		add_menu = sc.nextLine();
		System.out.println("������ ���Դϱ�?");
		menu_price = sc.nextInt();
		menu.put(add_menu, menu_price);
		sc.nextLine();

		File file = new File("./menu.txt");
		FileWriter writer = null;
		try{
			writer = new FileWriter(file, true);
			writer.write("\n" + add_menu + "\n" + menu_price);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(writer != null){
					writer.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		String add_menu_log = "";
		add_menu_log = "(add_menu)"+add_menu + "�� �޴��� �߰��ϼ̽��ϴ�.\n ";
		System_access.admin_log(add_menu_log);
	}
	//�޴��߰�

	public static void sub_menu(Map<String, Integer> menu){//txt���Ͽ����� �����ؾ��ϴµ� ����
		System.out.println("� �޴��� �����Ͻðڽ��ϱ�?");
		sub_menu = sc.nextLine();
		menu.remove(sub_menu);
		String sub_menu_log = "";
		sub_menu_log ="(sub_menu)"+ sub_menu + "�� �޴����� �����ϼ̽��ϴ�.\n ";
		System_access.admin_log(sub_menu_log);
	}
	//�޴�����

	public static void menu_find(Map<String, Integer> menu){
		for(String key : menu.keySet()){
			Integer value = menu.get(key);
			System.out.println(key+" : "+value);
		}
		System.out.println();
	}
}




















