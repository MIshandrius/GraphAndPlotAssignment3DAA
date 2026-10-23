package daa;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {
    private void referenceUnion(int[] groups, int a, int b){
        int groupA = groups[a];
        int groupB = groups[b];

        for(int i = 0; i< groups.length; i++){
            if(groups[i] == groupB){
                groups[i] = groupA;
            }
        }
    }

    private int referenceComponents(int[] groups){
        int count  = 0;

        for(int i = 0; i< groups.length;i++){
            boolean firstOccurence = true;

            for(int j = 0;j < i; j++){
                if(groups[i] == groups[j]){
                    firstOccurence = false;
                    break;
                }
            }
            if(firstOccurence){
                count++;
            }
        }
        return  count;
    }
    @Test
    void testInitialComponents(){
        DSU dsu = new DSU(5);

        assertEquals(5,dsu.components());
    }

    @Test
    void testUnion(){
        DSU dsu = new DSU(4);

        dsu.union(0, 1);
        dsu.union(2,3);

        assertEquals(2, dsu.components());
    }

    @Test
    void testFind(){
        DSU dsu = new DSU(4);

        dsu.union(0,1);
        dsu.union(1,2);

        assertEquals(dsu.find(0), dsu.find(0));
    }

    @Test
    void testRepeatedUnion(){
        DSU dsu = new DSU(3);

        assertTrue(dsu.union(0,1));

        assertFalse(dsu.union(0,1));
    }

    @Test
    void testCounters(){
        DSU dsu = new DSU(3);

        dsu.find(0);
        dsu.find(1);

        assertEquals(2, dsu.getFindCalls());
        assertEquals(0, dsu.getSuccessfulUnions());
    }

    @Test
    void testFindAfterUnions(){
        DSU dsu = new DSU(4);

        dsu.union(0,1);
        dsu.union(2,3);
        dsu.union(1,3);

        assertEquals(dsu.find(0), dsu.find(3));
        assertEquals(1, dsu.components());
    }

    @Test
    void testRandomUnions(){
        int n = 20;

        DSU dsu = new DSU(n);
        int[] groups  =new int[n];

        for(int i = 0; i< n;i++){
            groups[i] = i;
        }

        Random rnd = new Random(42);

        for (int step = 0; step < 100;step++){
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            dsu.union(a, b);
            referenceUnion(groups,a ,b);

            for(int i = 0; i < n;i++){
                for(int j = 0; j< n;j++){
                    boolean dsuSame = dsu.find(i) == dsu.find(j);
                    boolean refSame = groups[i] == groups[j];

                    assertEquals(refSame, dsuSame);

                }
            }
            assertEquals(referenceComponents(groups), dsu.components());


        }
    }

    @Test
    void testPathCompression() throws Exception{
        DSU dsu = new DSU(4);

        Field field = DSU.class.getDeclaredField("parent");
        field.setAccessible(true);

        int[] parent = (int[]) field.get(dsu);

        parent[0] = 0;
        parent[1] = 0;
        parent[2] = 1;
        parent[3] = 2;

        assertEquals(0, dsu.find(3));

        assertArrayEquals(new int[]{0,0,0,0},
        parent);
    }

}
