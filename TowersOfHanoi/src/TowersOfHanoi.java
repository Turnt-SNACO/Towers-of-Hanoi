public class TowersOfHanoi {
	private int numberOfDiscs;
	public int[][] towers;
	
	public TowersOfHanoi(int numberOfDiscs){
		this.numberOfDiscs=numberOfDiscs;
		this.towers=new int[numberOfDiscs][3];
		//Populate the 2D array with the discs from smallest to largest 
		//(top down) on the first peg
		for (int x=0;x<numberOfDiscs;x++){
			towers[x][0]=x+1;
		}
	}
	
	//relay to the recursive function topDiscRec
	//returns the topmost occupied slot in the vertical of towers array
	public int topDisc(int peg){
		return topDiscRec(peg, numberOfDiscs-1);
	}
	
	//Recursive function to find top disc
	private int topDiscRec(int peg, int top){
		if (towers[top][peg]==0)
			return top+1;
		else if (towers[top][peg]==1)
			return top;
		else
			return topDiscRec(peg,top-1);
	}
	
	//move disc from source peg to destination peg
	public void moveDisc(int source, int destination){
		towers[topDisc(destination)-1][destination]=towers[topDisc(source)][source];
		towers[topDisc(source)][source] = 0;
	}
	
	//feed arg numDiscs as numberOfDiscs
	//for general purposes start, aux, and end args should be fed 0,1,2
	//this will display every move made through the console
	public void moveTower(int numDiscs,int start, int aux, int end){
		if (numDiscs>=1){
			moveTower(numDiscs-1,start,end,aux);
			for (int x=0;x<getNumberOfDiscs();x++){
				for (int y=0;y<3;y++){
					if (towers[x][y]==0)
						System.out.print(" | ");
					else
						System.out.print(" "+towers[x][y]+" ");
				}
				System.out.println();
			}
			System.out.println("---------");
			moveDisc(start,end);
			moveTower(numDiscs-1,aux,start,end);
		}
		else if (numDiscs==1)
			moveDisc(start,end);
	}
	
	public int getNumberOfDiscs() {
		return numberOfDiscs;
	}
}