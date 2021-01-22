package concurrency;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ADaemon {
    public static void main(String[] args) {
        System.out.println("ddd");
//        search(new ArrayList<>(), null, null, 0, 4);
        System.out.println(nthPersonGetsNthSeat(25));
    }

    static int count = 0;

    public static double nthPersonGetsNthSeat(int n) {
        count = 0;
        List<int[]> list = new ArrayList();
        int[] array = new int[n];
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            used[i] = true;
            search(list, array, used, 1, n);
            used[i] = false;
        }

        System.out.println("count = " + count);
        System.out.println("size = " + list.size());
        return count * 1.0d / list.size();
    }

    /**
     * @param result 所有结果
     * @param array  当前的数组结果
     * @param used   已用的空格数
     * @param value  待设置的值
     * @param n      总人数
     */
    private static void search(List<int[]> result, @NotNull int[] array, @NotNull boolean[] used, int value, int n) {
        if (value == n) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = array[i];
            }
//            for (int i = 0; i < n; i++) {
//                System.out.print(a[i]);
//            }
//            System.out.println("-------");
            if (a[n - 1] == n - 1) {
                count++;
            }
            result.add(a);
            return;
        }
        if (!used[value]) {
            array[value] = value;
            used[value] = true;
            value++;
            search(result, array, used, value, n);
            --value;
            array[value] = 0;
            used[value] = false;
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                array[i] = value;
                used[i] = true;
                value++;
                search(result, array, used, value, n);
                value--;
                array[i] = 0;
                used[i] = false;
            }
        }
    }
}
