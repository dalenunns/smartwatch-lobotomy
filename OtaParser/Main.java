import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static boolean writeCommandLine = false;
    public static void main(String[] args) {
        System.out.println("Telink OTA File Parser");
        FileInputStream in = null;

        if (args.length <= 0) {
            System.out.println("  ERROR - No firmware image supplied to parse.");
            return;
        } 
        
        String filename = args[0];
        String macAddress = "";
        String wrtHandle = "";

        System.out.println(" Parsing File: " + filename);
   


        if (args.length == 3) {
            writeCommandLine =true;
            macAddress = args[1];
            wrtHandle = args[2];
            MyLogger.ENABLE = false;
            System.out.println(" Using MAC address: " + macAddress);
            System.out.println(" Using Handle: " + wrtHandle);
        }

        try {
            in = new FileInputStream(filename);
            byte[] firmware = in.readAllBytes();

            OtaPacketParser mOtaParser = new OtaPacketParser();
            
            byte[] prepareOtaCommandPkt = prepareOtaCommand();
            byte[] startOtaCommandPkt = startOtaCommand();

            MyLogger.d("Prepare Packet: "+bytesToHex(prepareOtaCommandPkt));
            printGatttoolCmdLine(macAddress, wrtHandle, bytesToHex(prepareOtaCommandPkt));
            MyLogger.d("Start Packet: "+bytesToHex(startOtaCommandPkt));
            printGatttoolCmdLine(macAddress, wrtHandle, bytesToHex(startOtaCommandPkt));

            mOtaParser.set(firmware); //Set the firmware we're going to be working with
            while (mOtaParser.hasNextPacket()) {
                byte[] nextPkt = mOtaParser.getNextPacket();

                MyLogger.d("Data Packet: "+bytesToHex(nextPkt));
                printGatttoolCmdLine(macAddress, wrtHandle, bytesToHex(nextPkt));
            }

            byte[] endOtaCommandPkt = endOtaCommand(mOtaParser);

            MyLogger.d("End Packet: "+bytesToHex(endOtaCommandPkt));
            printGatttoolCmdLine(macAddress, wrtHandle, bytesToHex(endOtaCommandPkt));


        } catch (Exception e) {

            e.printStackTrace();
        } finally { 
            if (in !=null )  {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }    
    }

    public static void printGatttoolCmdLine(String macAddress, String handle, String hexBytes) {
        if (writeCommandLine)
            System.out.println("gatttool -b "+macAddress+" --char-write-req --handle="+handle+" --value="+hexBytes);
    }

    public static byte[] prepareOtaCommand() {
        return new byte[]{0, -1};
    }

    public static byte[] startOtaCommand() {
        return new byte[]{1, -1};
    }

    public static byte[] endOtaCommand(OtaPacketParser mOtaParser) {
        int index = mOtaParser.getIndex();
        int i = ~index;
        byte[] bArr = {2, -1, (byte) (index & 255), (byte) ((index >> 8) & 255), (byte) (i & 255), (byte) ((i >> 8) & 255)};
        mOtaParser.fillCrc(bArr, mOtaParser.crc16(bArr));
        return bArr;
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
