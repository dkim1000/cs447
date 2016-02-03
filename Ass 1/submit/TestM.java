import org.junit.*;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class TestM {

	private M m;
	private ByteArrayOutputStream outStream = new ByteArrayOutputStream();

	private String case0;
	private String case1;
	private String case2;
	private String caseDefault;

	String zero = "zero\n";
	String A_OUTPUT = "a\n";
	String B_OUTPUT = "b\n";

	
    public TestM() {
	}

	@Before
	public void setup() {
		m = new M();
		System.setOut(new PrintStream(outStream));

		case0 = "";
		case1 = "a";
		case2 = "aa";
		caseDefault = "aaaaaa";
	}

	@After
	public void tearDown() {
		m = null;
		System.setOut(null);
	}


	//Node Coverage Tests
	@Test
	public void testNC2() {
		m.m(case2, 0);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	@Test
	public void testNC1() {
		m.m(case1, 0);
		assertEquals(A_OUTPUT, outStream.toString());
	}

	@Test
	public void testNC0() {
		m.m(case0, 0);
		assertEquals(zero, outStream.toString());
	}

	//Edge Coverage Tests
	//TR: [0,2],[2,6],[6,7],[7,8],[8,10]
	@Test
	public void testEC4() {
		m.m(caseDefault, 1);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,2],[2,5],[5,6],[6,7],[7,8],[8,10]
	@Test
	public void testEC3() {
		m.m(case2, 1);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,2],[2,4],[4,7],[7,8],[8,10]
	@Test
	public void testEC2() {
		m.m(case1, 1);
		assertEquals(A_OUTPUT, outStream.toString());
	}

	//TR: [0,1],[1,2], [2,3],[3,7],[7,9],[9,10]
	@Test
	public void testEC1() {
		m.m(case0, 0);
		assertEquals(zero, outStream.toString());
	}
	
	/*
		Not possible to test edge pair coverage but not prime path coverage.
		Since this CFG prime path coverage covers all edge pairs, it is not
		needed to test edge pair coverage
	*/
	
	//Prime Path Coverage Tests

	//TR: [0,2,6,7,8,10]
	@Test
	public void testPPCDefault1() {
		m.m(caseDefault, 1);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,1,2,6,7,8,10]
	@Test
	public void testPPCDefault0() {
		m.m(caseDefault, 0);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,2,5,6,7,8,10]
	@Test
	public void testPPC5() {
		m.m(case2, 1);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,1,2,5,6,7,8,10]
	@Test
	public void testPPC4() {
		m.m(case2, 0);
		assertEquals(B_OUTPUT, outStream.toString());
	}

	//TR: [0,2,4,7,8,10]
	@Test
	public void testPPC3() {
		m.m(case1, 1);
		assertEquals(A_OUTPUT, outStream.toString());
	}

	//TR: [0,1,2,4,7,8,10]
	@Test
	public void testPPC2() {
		m.m(case1, 0);
		assertEquals(A_OUTPUT, outStream.toString());
	}

	//TR: [0,2,3,7,9,10]
	@Test
	public void testPPC1() {
		m.m(case0, 1);
		assertEquals(ZERO, outStream.toString());
	}

	//TR: [0,1,2,3,7,9,10]
	@Test
	public void testPPC0() {
		m.m(case0, 0);
		assertEquals(ZERO, outStream.toString());
	}
    
}

class M {
	public static void main(String [] argv){
		M obj = new M();
		if (argv.length > 0)
			obj.m(argv[0], argv.length);
	}
	
	public void m(String arg, int i) {
		int q = 1;
		A o = null;
		Impossible nothing = new Impossible();
		if (i == 0)
			q = 4;
		q++;
		switch (arg.length()) {
			case 0: q /= 2; break;
			case 1: o = new A(); new B(); q = 25; break;
			case 2: o = new A(); q = q * 100;
			default: o = new B(); break; 
		}
		if (arg.length() > 0) {
			o.m();
		} else {
			System.out.println("zero");
		}
		nothing.happened();
	}
}

class A {
	public void m() { 
		System.out.println("a");
	}
}

class B extends A {
	public void m() { 
		System.out.println("b");
	}
}

class Impossible{
	public void happened() {
		// "2b||!2b?", whatever the answer nothing happens here
	}
}
