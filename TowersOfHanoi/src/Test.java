public class Test {
	public static void main(String[] args) {
		TowersOfHanoi t = new TowersOfHanoi(3);
		t.moveTower(t.getNumberOfDiscs(),0,1,2);
		for (int x=0;x<t.getNumberOfDiscs();x++){
			for (int y=0;y<3;y++){
				if (t.towers[x][y]==0)
					System.out.print(" | ");
				else
					System.out.print(" "+t.towers[x][y]+" ");
			}
			System.out.println();
		}
	}
}
