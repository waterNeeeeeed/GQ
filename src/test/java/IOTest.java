import java.io.*;
import java.net.*;


class Person
	implements Serializable
{
	public String name;
	public int age;
	
	public Person(String a, int b){
		name = a;
		age = b;
	}
}

public class IOTest {
	public static void main(String[] args) 
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		/*ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
		Person p = new Person("gongtao", 23);
		oos.writeObject(p);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.txt"));
		Person p1 = (Person)(ois.readObject());
		System.out.println(p1.name + p1.age);*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		Person p = new Person("gongtao", 23);
		oos.writeObject(p);
		byte[] b = baos.toByteArray();
		System.out.println(b);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b, 0, 4096));
		Person p1 = (Person)ois.readObject();
		System.out.println(p1.name + p1.age);
		
		String ip = "192.168.194.66";
		InetAddress ipa = InetAddress.getByName(ip);
		//System.out.println(ipa.getHostAddress() + "\n" + InetAddress.getLocalHost() + "\n" + ipa.getLocalHost());
		System.out.println(InetAddress.getLocalHost() +  "\n" + InetAddress.getLocalHost().getHostAddress());
		//System.out.println(ipa);
	}
}
