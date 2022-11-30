class VibrateCommand {

    /*
     * Generates the command string to turn on Vibrate for the watch.
     */
    public static void main(String[] args) {
        var b = new byte[]{-51, 0, 6, 18, 1, 11, 0, 1, 1};
        System.out.println(bytesToHex(b));
    }
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
