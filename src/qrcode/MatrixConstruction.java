package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	 public static final int B = 0xFF_00_00_00;
	
	 public static final int W = 0xFF_FF_FF_FF;

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes
	 
	 public static final int R = 0xFF_FF_00_00;
	 
	 public static final int G = 0xFF_00_FF_00;

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	
	//This method uses all other methods in order to construct a matrix containing all the standard information.
	public static int[][] constructMatrix(int version, int mask) {
		int[][] matrix1 = initializeMatrix(version);
		addFinderPatterns(matrix1);
		addAlignmentPatterns(matrix1, version);
		addTimingPatterns(matrix1);
		addDarkModule(matrix1);
		addFormatInformation(matrix1, mask);
		return matrix1;
	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	
	//This method creates an empty 2d array, the size of which depends on the version of the QR code.
	public static int[][] initializeMatrix(int version) {
		int[][] Matrix = new int[QRCodeInfos.getMatrixSize(version)][QRCodeInfos.getMatrixSize(version)];
		return Matrix;
	}

	
	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	
	//This method calls the "addFPattern" method with the different coordinates of the finder patterns. 
	//The coordinates are those of the center pixel of each of the finder patterns.
	public static void addFinderPatterns(int[][] matrix) {
		int size = matrix[0].length;
		int x1 = 3;
		int y1 = 3;
		int x2 = size - 4;
		int y2 = 3;
		int x3 = 3;
		int y3 = size -4;
		addFPattern(matrix, x1, y1, 1);
		addFPattern(matrix, x2, y2, 2);
		addFPattern(matrix, x3, y3, 3);
	}
	
	//This method uses the four areas of a finder pattern to color the different pixels of the matrix.
	public static void addFPattern(int[][] matrix, int x, int y, int corner) {
		int a;
		int[][] Area1 = patternAreas(x, y, 1, corner);
		int[][] Area2 = patternAreas(x, y, 2, corner);
		int[][] Area3 = patternAreas(x, y, 3, corner);
		int[][] Area4 = patternAreas(x, y, 4, corner);
		
		for (a = 0; a < Area1.length; a++) {
			matrix[Area1[a][0]][Area1[a][1]] = B;
		}
		for (a = 0; a < Area2.length; a++) {
			matrix[Area2[a][0]][Area2[a][1]] = W;
		}
		for (a = 0; a < Area3.length; a++) {
			matrix[Area3[a][0]][Area3[a][1]] = B;
		}
		for (a = 0; a < Area4.length; a++) {
			matrix[Area4[a][0]][Area4[a][1]] = W;
		}
			
	}
		
	
	//This method contains the coordinates of all patterns, and returns them according the the index and corner as input.
	public static int[][] patternAreas(int x, int y, int index, int corner) {
		int[][] fB1 = {{x,y}, {x+1, y}, {x-1, y}, {x, y-1}, {x-1, y-1}, {x+1, y-1}, {x, y+1}, {x-1, y+1}, {x+1, y+1}}; 
		
		int[][] fW1 = {{x-2, y}, {x-2, y-1}, {x-2, y-2}, {x-2, y+1}, {x-2, y+2}, {x+2, y}, {x+2, y-1}, {x+2, y-2},
					  {x+2, y+1}, {x+2, y+2}, {x, y+2}, {x-1, y+2}, {x+1, y+2}, {x, y-2}, {x-1, y-2}, {x+1, y-2}};
		
		int[][] fB2 = {{x-3, y}, {x-3, y-1}, {x-3, y-2}, {x-3, y-3}, {x-3, y+1}, {x-3, y+2}, {x-3, y+3},
				      {x+3, y}, {x+3, y-1}, {x+3, y-2}, {x+3, y-3}, {x+3, y+1}, {x+3, y+2}, {x+3, y+3},
				      {x, y+3}, {x+1, y+3}, {x+2, y+3}, {x-1, y+3}, {x-2, y+3},	
				      {x, y-3}, {x+1, y-3}, {x+2, y-3}, {x-1, y-3}, {x-2, y-3}};
		
		int[][] fW2_1 = {{x+4, y}, {x+4, y-1}, {x+4, y-2}, {x+4, y-3}, {x+4, y+1}, {x+4, y+2}, {x+4, y+3}, {x+4, y+4},
			       {x, y+4}, {x+1, y+4}, {x+2, y+4}, {x+3, y+4}, {x-1, y+4}, {x-2, y+4}, {x-3, y+4}};
	
		int[][] fW2_2 = {{x-4, y}, {x-4, y-1}, {x-4, y-2}, {x-4, y-3}, {x-4, y+1}, {x-4, y+2}, {x-4, y+3}, {x-4, y+4},
		      		{x, y+4}, {x+1, y+4}, {x+2, y+4}, {x+3, y+4}, {x-1, y+4}, {x-2, y+4}, {x-3, y+4}};
	
		int[][] fW2_3 = {{x+4, y}, {x+4, y-1}, {x+4, y-2}, {x+4, y-3}, {x+4, y-4}, {x+4, y+1}, {x+4, y+2}, {x+4, y+3},
		      		{x, y-4}, {x+1, y-4}, {x+2, y-4}, {x+3, y-4}, {x-1, y-4}, {x-2, y-4}, {x-3, y-4}};
		
		int[][] aB1 = {{x,y}};
				
		int[][] aW = {{x-1, y}, {x-1, y+1}, {x-1, y-1}, {x+1, y}, {x+1, y+1}, {x+1, y-1}, {x, y+1}, {x, y-1}};
		
		int[][] aB2 = {{x-2, y}, {x-2, y+1},{x-2, y+2}, {x-2, y-1}, {x-2, y-2},
					   {x+2, y}, {x+2, y+1},{x+2, y+2}, {x+2, y-1}, {x+2, y-2},
					   {x, y-2}, {x-1, y-2}, {x+1, y-2},{x, y+2}, {x-1, y+2}, {x+1, y+2}};
		
		int[][] tB = {{x, y+2}, {x, y+4}, {x, y+6}, {x, y+8}, {x, y+10}, {x, y+12}, {x, y+14}, {x, y+16}, {x, y+18},
					  {x+2, y}, {x+4, y}, {x+6, y}, {x+8, y}, {x+10, y}, {x+12, y}, {x+14, y}, {x+16, y}, {x+18, y}};
				
		int[][] tW = {{x, y+3}, {x, y+5}, {x, y+7}, {x, y+9}, {x, y+11}, {x, y+13}, {x, y+15}, {x, y+17},
					  {x+3, y}, {x+5, y}, {x+7, y}, {x+9, y}, {x+11, y}, {x+13, y}, {x+15, y}, {x+17, y}};
		
		int[][] info1 = {{x-7, y+1}, {x-6, y+1}, {x-5, y+1}, {x-4, y+1}, {x-3, y+1}, {x-2, y+1}, {x, y+1}, {x+1, y+1},
					     {x+1, y}, {x+1, y-2}, {x+1, y-3}, {x+1, y-4}, {x+1, y-5}, {x+1, y-6}, {x+1, y-7}};
		
		int[][] info2_1 = {{x+1, y+7}, {x+1, y+6}, {x+1, y+5}, {x+1, y+4}, {x+1, y+3}, {x+1, y+2}, {x+1, y+1}};
				
		int[][] info2_2 = {{x, y+1}, {x+1, y+1}, {x+2, y+1}, {x+3, y+1}, {x+4, y+1}, {x+5, y+1}, {x+6, y+1}, {x+7, y+1}};
				
		switch (index) {
			case 1:
				return fB1;

			case 2:
				return fW1;

			case 3:
				return fB2;

			case 4:
				switch(corner) {
					case 1:
						return fW2_1;
					case 2:
						return fW2_2;
					case 3:
						return fW2_3;
				}
			
			case 5:
				return aB1;
				
			case 6:
				return aW;
				
			case 7:
				return aB2;
				
			case 8:
				return tB;
				
			case 9:
				return tW;
				
			case 10:
				return info1;
				
			case 11:
				return info2_1;
				
			case 12:
				return info2_2;
			
				
			default:
				return null;
		}
	}
	
	 /**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	
	//This method calls the "addAPattern" method with the coordinates of the alignment pattern. 
	//The coordinates are those of the center pixel the finder pattern.
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		if (version > 1) {
			int xy = matrix.length - 7;
			addAPattern(matrix,xy, xy, 0);
		}
	}
	
	//This method uses the three areas of an alignment pattern to color the different pixels of the matrix.
	public static void addAPattern(int[][] matrix, int x, int y, int corner) {
		int a;
		int[][] Area1 = patternAreas(x, y, 5, corner);
		int[][] Area2 = patternAreas(x, y, 6, corner);
		int[][] Area3 = patternAreas(x, y, 7, corner);
		
		for (a = 0; a < Area1.length; a++) {
			matrix[Area1[a][0]][Area1[a][1]] = B;
		}
		for (a = 0; a < Area2.length; a++) {
			matrix[Area2[a][0]][Area2[a][1]] = W;
		}
		for (a = 0; a < Area3.length; a++) {
			matrix[Area3[a][0]][Area3[a][1]] = B;
		}
	}
	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	
	//This method adds the alignment pattern. It uses the pixel with coordinates (6,6) as center pixel and alternates black and white pixels from there.
	public static void addTimingPatterns(int[][] matrix) {
		int xy = 6;
		int a = 6;	
		do {
			a +=2;
			matrix[a][6] = B;
		} while(matrix.length - a > 8);
		a = 7;
		do {
			a +=2;
			matrix[a][6] = W;
		} while(matrix.length - a > 8);
		a = 6;
		do {
			a +=2;
			matrix[6][a] = B;
		} while(matrix.length - a > 8);
		a = 7;
		do {
			a +=2;
			matrix[6][a] = W;
		} while(matrix.length - a > 8);
	}
	
	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	
	//This method adds the dark module in the matrix.
	public static void addDarkModule(int[][] matrix) {
		int x = 8;
		int y = matrix.length -8;
		matrix[x][y] = B;
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	
	//This method uses the "addIPattern" and 
	public static void addFormatInformation(int[][] matrix, int mask) {
		boolean[] infoBits = QRCodeInfos.getFormatSequence(mask);
		addIPattern(matrix, 7, 7, 7, matrix.length - 8, matrix.length - 8, 7, 0, infoBits);
	}
	
	//This method uses the format sequence bits and the areas for the format sequence in correlation to color the pixels according to the format sequence bits.
	public static void addIPattern(int[][] matrix, int x1, int y1, int x2, int y2, int x3, int y3, int corner, boolean[] infoBits) {
		int a;
		int b = 0;
		int[][] Area1 = patternAreas(x1, y1, 10, corner);
		int[][] Area2 = patternAreas(x2, y2, 11, corner);
		int[][] Area3 = patternAreas(x3, y3, 12, corner);
		
		for (a = 0; a < Area1.length; a++) {
			if (infoBits[a] == false) {
				matrix[Area1[a][0]][Area1[a][1]] = W;
			} else {
				matrix[Area1[a][0]][Area1[a][1]] = B;
			}
		}
		
		for (a = 0; a < Area2.length; a++) {
			if (infoBits[a] == false) {
				matrix[Area2[a][0]][Area2[a][1]] = W;
			} else {
				matrix[Area2[a][0]][Area2[a][1]] = B;
			}	
			b++;
		}
		for (a = 0; a < Area3.length; a++) {
			if (infoBits[b] == false) {
				matrix[Area3[a][0]][Area3[a][1]] = W;
			} else {
				matrix[Area3[a][0]][Area3[a][1]] = B;
			}
			b++;
		}
	}
	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	
	//This method regroups all the different formulas for the different masks and returns the correct bit color.
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		if (masking < 0 || masking > 7) {
			if (dataBit == false) {
				return W;
			}
			return B;
		}
		
		boolean bit;
		
		switch(masking) {
			case 0:
				bit = (col + row) % 2 == 0;
				break;
			case 1:
				bit = (row % 2) == 0;
				break;
			case 2:
				bit = (col % 3) == 0;
				break;
			case 3:
				bit = (col + row) % 3 == 0;
				break;
			case 4:
				bit = ((Math.floor(row / 2) + Math.floor(col  /3)) % 2) == 0;
				break;
			case 5:
				bit = (((col * row) % 2) + ((col * row) % 3)) == 0;
				break;
			case 6:
				bit = (((col * row) % 2) + ((col * row) % 3)) % 2 == 0;
				break;
			case 7:
				bit = (((col + row) % 2) + ((col * row) % 3)) % 2 == 0;
				break;
			default:
				bit = false;
		}
		
		if (bit == true && dataBit == true) {
			return W;
		} else if (bit == true && dataBit == false) {
			return B;
		} else if (dataBit == false) {
			return W;
		}
		return B;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	
	//This method reads the path generated by the "createPath" method to color the pixels of the matrix.
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		int[][] path = createPath(matrix, data, mask);
		int a;
		
		for (a = 0; a < path.length; a++) {
			if (path[a][2] == W) {
				matrix[path[a][0]][path[a][1]] = W; //debug : red equals white
			} else if (path[a][2] == B){
				matrix[path[a][0]][path[a][1]] = B; //debug : green equals black
			}
		}
	}
	
	//This method creates a 2d integer array. 
	//Each pixel in the array contains the xy coordinates of the pixel and the color it needs to be according to the boolean array in input.
	public static int[][] createPath(int[][] matrix, boolean[] data, int mask) {
		int[][] infoPath = new int[matrix.length * (matrix.length - 1)][3];
		int a, c = 0;
		double b;
		for (a = matrix.length - 1; a > 0; a -= 4) {
			for (b = matrix.length - 1; b > -1; b -= 0.5) {
					if (matrix[a][(int)Math.ceil(b)] != B && matrix[a][(int)Math.ceil(b)] != W) {
						if (c % 2 == 1 && c != 0) {
							a--;
						}
						infoPath[c][0] = a;
						infoPath[c][1] = (int)Math.ceil(b);
						
						if (c < data.length) {
							infoPath[c][2] = maskColor(a, (int)Math.ceil(b), data[c], mask);
						} else {
							infoPath[c][2] = maskColor(a, (int)Math.ceil(b), false, mask);
						}
						
						if (c % 2 == 1 && c != 0) {
							a++;
						}
						c++;
					} else if ((matrix[a][(int)Math.ceil(b)] == B || matrix[a][(int)Math.ceil(b)] == W) && matrix[a - 1][(int)Math.ceil(b)] != B && matrix[a - 1][(int)Math.ceil(b)] != W){
						
						a--;
						b -= 0.5;
						infoPath[c][0] = a;
						infoPath[c][1] = (int)Math.ceil(b);
						
						if (c < data.length) {
							infoPath[c][2] = maskColor(a, (int)Math.ceil(b), data[c], mask);
						} else {
							infoPath[c][2] = maskColor(a, (int)Math.ceil(b), false, mask);
						}
						
						a++;
						c++;
					}
			}
				
			
			a -= 2;
			
			if (a == 6) {
				a--;
			}
			
			for (b = 0; b < matrix.length; b += 0.5) {
				if (matrix[a][(int)Math.floor(b)] != B && matrix[a][(int)Math.floor(b)] != W) {
					if (c % 2 == 1 && c != 0) {
						a--;
					}
					infoPath[c][0] = a;
					infoPath[c][1] = (int)Math.floor(b);
					
					if (c < data.length) {
						infoPath[c][2] = maskColor(a, (int)Math.floor(b), data[c], mask);
					} else {
						infoPath[c][2] = maskColor(a, (int)Math.floor(b), false, mask);
					}
					
					if (c % 2 == 1 && c != 0) {
						a++;
					}
					c++;
				}
			}
			
			a += 2;
			
		}
		return infoPath;
	}
	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
	
		return 0;
	}

}
