package yahtzee;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptimalStatebasedAlgorithmTest {

	@Test
	public void containsTest() {
		assertEquals(true, OptimalStatebasedAlgorithm.contains(new short[]{1,2,2,2,2},new short[]{1,2}));
		assertEquals(true, OptimalStatebasedAlgorithm.contains(new short[]{1,2,2,2,2},new short[]{2,2}));
		assertEquals(true, OptimalStatebasedAlgorithm.contains(new short[]{1,2,3,4,5},new short[]{4,5}));
		assertEquals(true, OptimalStatebasedAlgorithm.contains(new short[]{1,2,3,4,5},new short[]{5,4}));
		assertEquals(false, OptimalStatebasedAlgorithm.contains(new short[]{1,2,3,4,5},new short[]{2,2,2}));
	}
	
	@Test
	public void probabilityTest() {
		OptimalStatebasedAlgorithm.Element V2 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,1,1,1});
		OptimalStatebasedAlgorithm.Element V4 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,1,1,1,1});
		assertEquals((double)1.0/6,OptimalStatebasedAlgorithm.probability(V2, V4),1E-10);
		
		V2 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{2,2,2,2,2});
		V4 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,1,1,1,1});
		assertEquals((double)Math.pow(1.0/6,5),OptimalStatebasedAlgorithm.probability(V2, V4),1E-10);
		
		V2 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{2,2,1,1,1});
		V4 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,1,1,1,1});
		assertEquals((double)1.0/36,OptimalStatebasedAlgorithm.probability(V2, V4),1E-10);		
		
		V2 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		V4 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		assertEquals((double)1.0,OptimalStatebasedAlgorithm.probability(V2, V4),1E-10);
		
		V2 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,4});
		V4 = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		assertEquals((double)1.0/6,OptimalStatebasedAlgorithm.probability(V2, V4),1E-10);
		
		OptimalStatebasedAlgorithm.Element v = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{2,3,4,5});
		OptimalStatebasedAlgorithm.Element u = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		assertEquals(true, OptimalStatebasedAlgorithm.contains(u.dices,v.dices));
		
		assertEquals((double)1.0/6,OptimalStatebasedAlgorithm.probability(v, u),1E-10);
		
		v = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{4,5});
		u = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		assertEquals(true, OptimalStatebasedAlgorithm.contains(u.dices,v.dices));
		
		assertEquals((double)1.0/Math.pow(6,3),OptimalStatebasedAlgorithm.probability(v, u),1E-10);
		
		v = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2});
		u = new OptimalStatebasedAlgorithm.Element(new OptimalStatebasedAlgorithm.Tuple(new boolean[15], 0), new short[]{1,2,3,4,5});
		assertEquals(true, OptimalStatebasedAlgorithm.contains(u.dices,v.dices));
		
		assertEquals((double)1.0/Math.pow(6,3),OptimalStatebasedAlgorithm.probability(v, u),1E-10);
	}
	
	@Test
	public void playTest(){
		OptimalStatebasedAlgorithm osa = new OptimalStatebasedAlgorithm();
		System.out.println(osa.X(osa.V1[0]));
		
	}

}
