package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import simu.model.SimulaattorinParametrit;

class SimulaattorinParametritTest {

	private static SimulaattorinParametrit sp;
	private double valitallennus;
	
	@BeforeAll
	private static void  setUp() {
		sp = SimulaattorinParametrit.getInstance();
	}
	
	@AfterEach
	void nollaa() {
		valitallennus = 0;
		sp.setOletusarvot();
	}
	
	@Test
	@Order(1)
	void test() {
		valitallennus = sp.getErikoiskalibKMAika();
		sp.setErikoiskalibKMAika(100.0);
		sp.setOletusarvot();
		assertEquals(valitallennus, sp.getErikoiskalibKMAika(), "Ainokainen ei palautunut oletusarvoihin");
	}
	@Test
	@Order(2)
	void test2() {
		valitallennus = 1234.567;
		sp.setSimulaatioAikaTunteina(valitallennus);
		
		assertEquals(valitallennus, sp.getSimulaatioaikaTunteina(), "Ainokainen ei tallentanut arvoa");
	}
	@Test
	@Order(3)
	void test3() {
		sp.setSimulaatioAikaTunteina(10);
		valitallennus = sp.getSimulaatioaikaMinuutteina();
		assertEquals(valitallennus, (10*60), "Ainokainen laski minuutit väärin");
	}

}
