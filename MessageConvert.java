import java.nio.ByteBuffer;

class MessageConvert {
    public static void main(String[] args) {
        
        // 1 - SMS
        // 2 - QQ
        // 3 - WeChat
        // 4 - Facebook
        // 5 - Twitter
        // 6 - Skype
        // 7 - Line!
        // 8 - WhatsApp
        // 9 - Kakao Talk
        // 16 - Instagram
        // 17 - LinkedIn
        // You can specify the notification source by changing the first byte of this array, using the values above.
        var messageType = new byte[]{1,0,0}; //SMS
        var source = "Cereal"; //Phone number / Person that the message came from.
        var message = "Hack the Planet"; //Message
        
        ByteBuffer msgBuf = ByteBuffer.allocate(3 + source.length() + 1 + message.length());
        msgBuf.put(messageType);
        msgBuf.put(source.getBytes());
        msgBuf.put((byte) 58); //:
        msgBuf.put(message.getBytes());
        
        var messageBytes = msgBuf.array();
        //Generate the byte array for the output notification message.
        var outputMsg = getProtocol((byte)18, (byte)18, messageBytes);
        //Print the message to the screen as hex
        System.out.println("message: " + bytesToHex(outputMsg));


        //Generate a command to turn on notifications for all providers.
        var messageNotificationsCMDArray = getProtocol((byte)18,(byte)7, new byte[]{1,1,1,1,1,1,1,1,1,1,1,1});
        //Print command to screen as hex
        System.out.println("command to turn on notifications: " +bytesToHex(messageNotificationsCMDArray));
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
        
    //Additional method from SendData required by getProcotol()
    public static Integer getLength() {
        return 8;
    }
    
    //from ByteUtil class
    public static byte[] intToBytes(int i) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) (i >> (24 - (i2 * 8)));
        }
        return bArr;
    }

    public static byte[] getProtocol(byte b, byte b2, byte[] bArr) {
        Integer valueOf = Integer.valueOf(getLength().intValue() + bArr.length);
        byte[] bArr2 = new byte[valueOf.intValue()];
        bArr2[0] = -51;
        byte[] intToBytes = intToBytes(valueOf.intValue() - 3);
        System.arraycopy(intToBytes, 2, bArr2, 1, intToBytes.length - 2);
        bArr2[3] = b;
        bArr2[4] = 1;
        bArr2[5] = b2;
        byte[] intToBytes2 = intToBytes(bArr.length);
        System.arraycopy(intToBytes2, 2, bArr2, 6, intToBytes2.length - 2);
        System.arraycopy(bArr, 0, bArr2, 8, bArr.length);
        return bArr2;
    }
}