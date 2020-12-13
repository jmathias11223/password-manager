
/**
 * This class handles converting DNA sequences and IDs to ASCII and back
 * 
 * @author Justin Mathias
 * @version 2020.12.03
 *
 */
public class Converter {
    /**
     * Converts the given String to a byte array using the project's conversions
     * for A, G, C, and T
     * 
     * @param data
     *            The String to convert
     * @return arr The binary representation of the String
     */
    public static byte[] toASCII(String data) {
        // First, initialize the byte array
        byte[] arr;
        if (data.length() % 4 == 0) {
            arr = new byte[data.length() / 4];
        }
        else {
            arr = new byte[data.length() / 4 + 1];
        }

        // Next, perform the conversion
        int count = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            byte first = (byte)(convertLetter(data.substring(count, count
                + 1)) << 6);
            byte second = (byte)(convertLetter(data.substring(count + 1, count
                + 2)) << 4);
            byte third = (byte)(convertLetter(data.substring(count + 2, count
                + 3)) << 2);
            byte fourth = (byte)(convertLetter(data.substring(count + 3, count
                + 4)));
            arr[i] = (byte)(first | second | third | fourth);
            count += 4;
        }

        // Handle last byte
        String lastChars = data.substring(count);
        byte lastByte = 0b00;
        // Variable count repurposed for counting number of letters converted in
        // this section now
        count = 0;
        while (lastChars.length() != 0) {
            lastByte = (byte)((lastByte << 2) | convertLetter(lastChars
                .substring(0, 1)));
            lastChars = lastChars.substring(1);
            count++;
        }
        arr[arr.length - 1] = (byte)(lastByte << ((4 - count) * 2));
        return arr;
    }


    /**
     * Converts a given byte array into a String using the project's conversion
     * parameters
     * 
     * @param data
     *            The byte array to be converted
     * @param length
     *            The length of the String to return
     * @return str The String representation of the binary
     */
    public static String toSequence(byte[] data, int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            str.append(convertByte(data[i]));
        }
        return str.toString().substring(0, length);
    }


    /**
     * Converts a letter to binary
     * 
     * A - 00
     * C - 01
     * G - 10
     * T - 11
     * 
     * @param letter
     *            String to be converted
     * @return The binary number
     */
    private static byte convertLetter(String letter) {
        switch (letter.toUpperCase()) {
            case "C":
                return 0b01;

            case "G":
                return 0b10;

            case "T":
                return 0b11;

            default:
                return 0b00;
        }
    }


    /**
     * Converts a byte to a series of characters
     * 
     * @param b
     *            The byte to convert
     * @return The String representation of the byte
     */
    private static String convertByte(byte b) {
        StringBuilder str = new StringBuilder();
        str.append(convertByteHelper((byte)(b & 0b11000000), 6));
        str.append(convertByteHelper((byte)(b & 0b00110000), 4));
        str.append(convertByteHelper((byte)(b & 0b00001100), 2));
        str.append(convertByteHelper((byte)(b & 0b00000011), 0));
        return str.toString();
    }


    /**
     * Helper method for convertByte(), converts the byte given to a letter
     * 
     * @param b
     *            The byte given
     * @param numShift
     *            Number to shift the byte by to get the correct letter
     * @return The letter represented by the byte
     */
    private static String convertByteHelper(byte b, int numShift) {
        if (b == (byte)(0b00 << numShift)) {
            return "A";
        }
        if (b == (byte)(0b01 << numShift)) {
            return "C";
        }
        if (b == (byte)(0b10 << numShift)) {
            return "G";
        }
        return "T";
    }

}
