package GQ;

import java.awt.Image;
import java.io.Serializable;


enum Gender
{
	MALE,FEMALE;
}

public class GQFriendInfo
	implements Serializable
{
	private String name;
	private Image portrait;
	private Image SVIP;
	private String sign;
	private Gender gender;
	private OnlineAddressID address;//TCP SERVER IP+PORT
	
	public GQFriendInfo(){
		
	}
	
	public GQFriendInfo(String name, Image portrait, String sign, Gender gender, 
			OnlineAddressID address){
		this.name = name;
		this.portrait = portrait;
		this.sign = sign;
		this.gender = gender;
		this.address = address;
	}
	
	public void setGoOnlineAddress(OnlineAddressID addr){
		address = addr;
	}
	
	public OnlineAddressID getGoOnlineAddress(){
		return address;
	}
	
	public void setGender(Gender g){
		gender = g;
	}
	
	public Gender getGender(){
		return gender;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPortrait(Image im){
		portrait = im;
	}
	
	public Image getPortrait(){
		return portrait;
	}
	
	public void setSVIP(Image im){
		SVIP = im;
	}
	
	public Image getSVIP(){
		return SVIP;
	}
	
	public void setSign(String s){
		sign = s;
	}
	
	public String getSign(){
		return sign;
	}
}