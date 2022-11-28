package ca.jaddua.pokemon;

import java.util.Arrays;

public class Damage {
	
	public int[] damage;
	
	public Damage(int level, int power, int attack, int defense, boolean stab, boolean critical, int type, int weather) {
		
		int damage = (((level << 1) / 5 + 2) * power * attack / defense / 50 + 2) * (stab ? 150 : 100) / 100 * (critical ? 150 : 100) / 100;
		
		if (type == -2)
			damage = damage * 25 / 100;
		else if (type == -1)
			damage = damage * 50 / 100;
		else if (type == 0)
			damage = 0;
		else if (type == 2)
			damage *= 2;
		else if (type == 3)
			damage *= 4;
		
		if (weather == 1)
			damage = damage * 150 / 100;
		else if (weather == -1)
			damage = damage * 50 / 100;
		
		this.damage = new int[] {damage * 85 / 100, damage};
	}
	
	public static void main(String[] args) {
		Damage dam = new Damage(100, 60, 591, 250, true, false, 1, 0);
		System.out.println("[ " + dam.damage[0] + " , " + dam.damage[1] + " ]");
	}
}
