import java.util.*;

public class Main {
    static class Solution {
        public static List<Integer> spiralOrder(int[][] matrix) {
            int r=matrix.length, rn=r;
            List<Integer>list=new ArrayList<>();
            if(r==0)return list;
            int c=matrix[0].length, cn=c;
            int cnt=1;
            list.add(matrix[0][0]);
            int x=0, y=0;
            int xn=0, yn=0;

            while(cnt<r*c){
                while(y+1<cn){
                    y++;
                    list.add(matrix[x][y]);
                    cnt++;
                }
                xn++;
                while(x+1<rn){
                    x++;
                    list.add(matrix[x][y]);
                    cnt++;
                }
                cn--;
                while(y-1>=yn){
                    y--;
                    list.add(matrix[x][y]);
                    cnt++;
                }
                rn--;
                while(x-1>=xn){
                    x--;
                    list.add(matrix[x][y]);
                    cnt++;
                }
                yn++;
            }
            return list;
        }
    }
    public static void main(String[] args) {
        int[][] arr={{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        Solution.spiralOrder(arr);
    }
}
