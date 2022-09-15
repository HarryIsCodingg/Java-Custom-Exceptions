
import java.io.*;
import java.util.*;

/**
 * 
 * @author HARINDER PARTAP SINGH 
 *
 */
public class CSV2HTML {
	static Scanner input = new Scanner(System.in);
	static PrintWriter exceptionWriter = null;
	static PrintWriter writer1 = null;
	static PrintWriter writer2 = null;
	static Scanner reader1 = null;
	static Scanner reader2 = null;
	static BufferedReader fileReader = null;
	static String attributes[] = null;
	static int dataMissingLine = 0;
	static boolean flag = false;
	static int counter = 0;
	static String htmlName=null;
	
	
	public static void main(String[] args) {


		System.out.printf(
				"\n*************************************************************************************************************\n"
						+ "\tWelcome To CSV to HTML Converter\n" + "\t\t\t\t\t\t    Designed By:Harinder Partap Singh\n"
						+ "*************************************************************************************************************\n");

		dataMissingLine = 0;
		File file = new File("D:\\CSVFiles");

		String[] files = file.list();
		File outputFiles = new File("D:\\outputFiles");
		String fileName = null;
		String htmlName1 = null;
		String htmlName2 = null;
		
		try {
			exceptionWriter = new PrintWriter(new FileOutputStream("D:\\outputFiles\\Exceptions.log", true));
			reader1 = new Scanner(new FileInputStream("D:\\CSVFiles\\" + files[0]));
			reader2 = new Scanner(new FileInputStream("D:\\CSVFiles\\" + files[1]));
		
			htmlName1 = files[0].split("-")[0] + ".html";
			htmlName2 = files[1].split("-")[0] + ".html";
			
			writer1 = new PrintWriter(new FileOutputStream("D:\\outputFiles\\" + htmlName1));
			writer2 = new PrintWriter(new FileOutputStream("D:\\outputFiles\\" + htmlName2));

			for (counter = 0; counter < files.length; counter++) {
				try {
					if (counter == 0) {
						fileName = files[0];
						htmlName=htmlName1;
						CSVToHTML(reader1, writer1, fileName);

					} else if (counter == 1) {
						fileName = files[1];
						htmlName=htmlName2;
						CSVToHTML(reader2, writer2, fileName);
					}
					
				} catch (AttributeMissingException e) {
					File deleteFile=new File("D:\\outputFiles\\"+htmlName);
					deleteFile.delete();
					writeExceptions(e.getMessage());
					exceptionWriter.close();
					continue;

				} catch (DataMissingException d) {

					writeExceptions(d.getMessage());
					
					while (!flag) {
						try {
							if (counter == 0) {
							
								checkDataMissing(reader1, writer1, fileName);
							} else if (counter == 1) {
								
								checkDataMissing(reader2, writer2, fileName);
							}
						} catch (DataMissingException e) {
							writeExceptions(e.getMessage());
						}
					}
				}
				
				System.out.printf("%s file has been successfully converted to HTML.\n",fileName);
				dataMissingLine = 0;
			}
			
			exceptionWriter.close();
			readFiles();
			
		}

		catch (FileNotFoundException e) {
			
			File[] outputFilesArray = outputFiles.listFiles();
			String fileNa = null;
			
			if (reader1 == null || reader2 == null) {
				if (reader1 == null) {
					fileNa = files[0];
				} else if (reader2 == null) {
					fileNa = files[1];
					reader1.close();
				}
				writeExceptions("Could not open file " + fileNa+" for reading.\nPlease check that the file exists and is readable.\n"
						+ "This program will terminate after closing any opened files.");
				
			} else if (writer1 == null) {
				
				reader1.close();
				reader2.close();
				writeExceptions("Could not open the output file " + htmlName1);
			
			// All the streams will be closed before deleting all the output files.
			} else if (writer2 == null) {
				reader1.close();
				reader2.close();
				writer1.close();
				exceptionWriter.close();
				for (int i = 0; i < outputFilesArray.length; i++) {
					outputFilesArray[i].delete();
				}
				writeExceptions("Could not open the output file " + htmlName2);
			}
			exceptionWriter.close();
			System.exit(0);
		}

		System.out.printf(
				"\n*************************************************************************************************************\n"
						+ "\tThe program has been successfully terminated.\n"
						+ "\t\t\t\t\t\t    Thankyou for using our services.\n"
						+ "*************************************************************************************************************\n");

		input.close();
	}

	/**
	 * 
	 * Displays the files in the directory.
	 * 
	 * @param files The files in the directory.
	 */
	public static void showFiles(String[] files) {
		for (int i = 0; i < files.length; i++) {
			System.out.printf("%d : %s\n", (i + 1), files[i]);
		}
	}

	/**
	 * Writes all the exceptions thrown in exceptions.log file
	 * 
	 * @param exception The name of the exception thrown.
	 */
	public static void writeExceptions(String exception) {
		System.out.println(exception);
		exceptionWriter.println(exception);

	}

	/**
	 * This is the main engine of the program and controls the reading of user
	 * defined CSS file and converting it to the HTML file.
	 * 
	 * @param scanner The scanner which reads the CSVfile.
	 * @param writer The PrintWriter which writes to HTML file.
	 * @param fileName The name of the file to be converted to HTML.
	 * @throws AttributeMissingException Throws exception if any of the attribute is
	 *                                   missing.
	 * @throws DataMissingException      Throws exception if any of the data is
	 *                                   missing.
	 */
	public static void CSVToHTML(Scanner scanner, PrintWriter writer, String fileName)
			throws AttributeMissingException, DataMissingException {
		System.out.printf("\n\t\t\tConverting the %s file to HTML file:\n\n",fileName);
		writer.println("<!DOCTYPE html>");
		writer.println("<html>\n<style>\n" + "table {font-family: arial, sans-serif;border-collapse:collapse;}"
				+ "\ntd, th {border: 1px solid #000000;text-align: left;padding: 8px;}"
				+ "\ntr:nth-child(even) {background-color: #dddddd;}" + "\nspan{font-size: small}"
				+ "\n</style>\n<body>");
		writer.println();
		writer.print("<table>\n<caption>Table 1: ");

		String heading[] = scanner.nextLine().split(",",-1);

		writer.print(heading[0]);
		

		writer.println("</caption>\n<tr>");

		attributes = scanner.nextLine().split(",", -1);
		for (String attribute : attributes) {
			if (attribute.isEmpty()) {
				writer.close();
				throw new AttributeMissingException(
						"Error: In file " + fileName + " Missing attribute.File is not converted to HTML.");
				
			}
			writer.println("<th>" + attribute + "</th>");
		}
		checkDataMissing(scanner, writer, fileName);
		
		scanner.close();
		writer.close();
	}

	/**
	 * Check if the data is missing from any line.
	 * 
	 * @param scanner The Scanner to read the CSV file.
	 * @param writer  The PrintWriter to write to output file.
	 * @param fileName The fileName to be read.
	 * @throws DataMissingException The data missing exception is thrown if the data is missing from any line.
	 */
	public static void checkDataMissing(Scanner scanner, PrintWriter writer, String fileName)
			throws DataMissingException {

		String line = scanner.nextLine();

		while (!line.contains("Note")) {
			dataMissingLine++;
			String[] tableRow = line.split(",", -1);
			for (int i = 0; i < tableRow.length; i++) {

				if (tableRow[i].isEmpty()) {

					throw new DataMissingException("Warning: In file " + fileName + " line " + dataMissingLine
							+ " is not converted to HTML : missing data: " + attributes[i]);
				}
			}

			createTables(writer, tableRow[0], tableRow[1], tableRow[2], tableRow[3]);
			line = scanner.nextLine();
		}

		writer.println("</table>\n<span>" + line.split(",")[0] + "</span>\n</body>\n</html>");
		writer.close();
		flag = true;

	}

	/**
	 * Creates tables of html file which includes four attributes.
	 * 
	 * @param writer The printwriter to write to output file.
	 * @param attribute1 The first attribute for the table of html.
	 * @param attribute2 The second attribute for the table of html.
	 * @param attribute3 The third attribute for the table of html.
	 * @param attribute4 The forth attribute for the table of html.
	 */
	public static void createTables(PrintWriter writer, String attribute1, String attribute2, String attribute3,
			String attribute4) {
		writer.println("<tr>");
		writer.println("  <td>" + attribute1 + "</td>");
		writer.println("  <td>" + attribute2 + "</td>");
		writer.println("  <td>" + attribute3 + "</td>");
		writer.println("  <td>" + attribute4 + "</td>");
		writer.println("</tr>");
	}

	/**
	 * Files are shown and a single output file is displayed based upon user selection.
	 * If user enters invalid output file then FileNotFound exception is thrown.
	 */
	public static void readFiles() {

		System.out.printf(
				"\n*************************************************************************************************************\n"
						+ "\t\t\t\tShowing the directory of the output files:\n"
						+ "*************************************************************************************************************\n");
		showFiles(new File("D:\\outputFiles").list());
		int counter = 0;

		while (counter < 2) {
			System.out.println("Enter the name of the file to be displayed: ");
			String fileName = input.next();
			try {
				fileReader = new BufferedReader(new FileReader("D:\\outputFiles\\" + fileName));


				System.out.printf(
						"\n*************************************************************************************************************\n"
								+ "\t\t\t\tDisplaying the %s file:\n"
								+ "*************************************************************************************************************\n",fileName);
				
				String line = fileReader.readLine();
				while (line != null) {
					System.out.println(line);
					line = fileReader.readLine();
				}
				fileReader.close();
				break;
			} catch (FileNotFoundException e) {
				writeExceptions(fileName + " file can't be opened. Try again.\n");
				if (counter == 1) {
					System.out.println(
							"\t\t\t\tSorry you have exhauseted all your options to open the file ");
				}
				counter++;

			} catch (IOException e) {
				System.out.println("Error loading the file");;
			}

		}
		
	}
}
