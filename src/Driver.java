import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * A Recursive Terrain Generation Class
 * @author mikeg
 * @version Dec 23, 2021
 */
public class Driver extends Canvas {

	final int DETAIL = 7; //Change this value to change the detail of the landscape
	//The next arrays will be the X, Y, and Z coordinates of each point
	//X - right and left
	//Y - up and down
	//Z - close and far
	final int[] POINT_A = new int[] {200, 400, 1000}; //Far left point of the chunk
	final int[] POINT_B = new int[] {200, 400, 200}; //Close left point
	final int[] POINT_C = new int[] {1000, 400, 200}; //Close right point
	final int[] POINT_D = new int[] {1000, 400, 1000}; //Far right point
	static final int VIEW_DISTANCE = 400; //Distance of viewer from terrain
	static final int VIEW_XPOS = 600; //X offset of viewer (600 = center)
	static final int VIEW_YPOS = 200; //Y offset of viewer
	
	// generateTerrain(Graphics g, int[] a, int[] b, int[] c, int[] d, int n)
	// is a recursive function that generates terrain on a 2D plane
	// g: graphics
	// a: far left point
	// b: close left point
	// c: close right point
	// d: far right point
	// n: detail of terrain
	public static void generateTerrain(Graphics g, int[] a, int[] b, int[] c, int[] d, int n) {
		
		//Calculates the sidelength of a lattice given the locations of 4 points (a, b, c, d) that form a quadrilateral
		int sideLength = (int) Math.round(Math.sqrt((b[0] - a[0]) * (b[0] - a[0]) + (b[2] - a[2]) * (b[2] - a[2])));
		
		//Once the accumulator variable n reaches 1, the lattices are drawn onto the screen
		if (n <= 1) {
			
			//Positions of the 4 lines that form a lattice are calculated using the constants
			//VIEW_XPOS, VIEW_YPOS, VIEW_DISTANCE using the linear interpolation formula to project
			//the 3d points onto the 2d plane
			int x1 = VIEW_XPOS, y1 = VIEW_YPOS, D = VIEW_DISTANCE;
			int ax, ay, bx, by, cx, cy, dx, dy;
			ax = x1 - ((x1 - a[0]) * D / a[2]);
			bx = x1 - ((x1 - b[0]) * D / b[2]);
			cx = x1 - ((x1 - c[0]) * D / c[2]);
			dx = x1 - ((x1 - d[0]) * D / d[2]);
			ay = y1 - ((y1 - a[1]) * D / a[2]);
			by = y1 - ((y1 - b[1]) * D / b[2]);
			cy = y1 - ((y1 - c[1]) * D / c[2]);
			dy = y1 - ((y1 - d[1]) * D / d[2]);
			
			//The lines are then drawn onto the 2D plane
			g.drawLine(ax, ay, bx, by);
			g.drawLine(bx, by, cx, cy);
			g.drawLine(cx, cy, dx, dy);
			g.drawLine(dx, dy, ax, ay);
		} else {
			//Continue to recurse if n has not yet reached the base case
			
			//Calculate displacement given the sidelength provided
			int displacement = (int) ((int) ((Math.random() * sideLength) - sideLength / 2));
			
			//Generate a new point m that will be created in the middle of an existing lattice
			//with a randomized y value
			int[] m = new int[] {(a[0] + b[0] + c[0] + d[0]) / 4, (a[1] + b[1] + c[1] + d[1]) / 4, (a[2] + b[2] + c[2] + d[2]) / 4};
			m[1] += displacement;
			
			//Creating 4 new points that represent the midpoints of each line forming the lattice
			int[] E = new int[] {(a[0] + b[0]) / 2, (a[1] + b[1]) / 2, (a[2] + b[2]) / 2};
			int[] F = new int[] {(b[0] + c[0]) / 2, (b[1] + c[1]) / 2, (b[2] + c[2]) / 2};
			int[] G = new int[] {(c[0] + d[0]) / 2, (c[1] + d[1]) / 2, (c[2] + d[2]) / 2};
			int[] H = new int[] {(d[0] + a[0]) / 2, (d[1] + a[1]) / 2, (d[2] + a[2]) / 2};
			
			//Recursing the function, replacing a, b, c, and d with a new quadrilateral 4 times,
			//each representing the 4 subsection that the square divides into
			generateTerrain(g, a, E, m, H, n - 1);
			generateTerrain(g, E, b, F, m, n - 1);
			generateTerrain(g, m, F, c, G, n - 1);
			generateTerrain(g, H, m, G, d, n - 1);
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Recursive Terrain Generation");
		Canvas canvas = new Driver();
		canvas.setSize(1200, 800);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void paint(Graphics g) {
		
		this.setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		
		generateTerrain(g, POINT_A, POINT_B, POINT_C, POINT_D, DETAIL);
	}

}
