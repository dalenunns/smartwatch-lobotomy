public class OtaPacketParser {
    private byte[] data;
    private int index = -1;
    private int progress;
    private int total;

    public void set(byte[] bArr) {
        clear();
        this.data = bArr;
        int length = bArr.length;
        if (length % 16 == 0) {
            this.total = length / 16;
        } else {
            this.total = (int) Math.floor((length / 16) + 1);
        }
    }

    public void clear() {
        this.progress = 0;
        this.total = 0;
        this.index = -1;
        this.data = null;
    }

    public byte[] getFirmwareVersion() {
        byte[] bArr = this.data;
        if (bArr.length < 6) {
            return null;
        }
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 2, bArr2, 0, 4);
        return bArr2;
    }

    public boolean hasNextPacket() {
        int i = this.total;
        return i > 0 && this.index + 1 < i;
    }

    public boolean isLast() {
        return this.index + 1 == this.total;
    }

    public int getNextPacketIndex() {
        return this.index + 1;
    }

    public byte[] getNextPacket() {
        int nextPacketIndex = getNextPacketIndex();
        byte[] packet = getPacket(nextPacketIndex);
        this.index = nextPacketIndex;
        return packet;
    }

    public byte[] getPacket(int i) {
        int length = this.data.length;
        if (length > 16) {
            length = i + 1 == this.total ? length - (i * 16) : 16;
        }
        int i2 = length + 4;
        byte[] bArr = new byte[20];
        for (int i3 = 0; i3 < 20; i3++) {
            bArr[i3] = -1;
        }
        System.arraycopy(this.data, i * 16, bArr, 2, i2 - 4);
        fillIndex(bArr, i);
        int crc16 = crc16(bArr);
        fillCrc(bArr, crc16);
        MyLogger.d("ota packet ---> index : " + i + " total : " + this.total + " crc : " + crc16 + " content : " + Arrays.bytesToHexString(bArr, ":"));
        return bArr;
    }

    public byte[] getCheckPacket() {
        byte[] bArr = new byte[16];
        for (int i = 0; i < 16; i++) {
            bArr[i] = -1;
        }
        int nextPacketIndex = getNextPacketIndex();
        fillIndex(bArr, nextPacketIndex);
        int crc16 = crc16(bArr);
        fillCrc(bArr, crc16);
        MyLogger.d("ota check packet ---> index : " + nextPacketIndex + " crc : " + crc16 + " content : " + Arrays.bytesToHexString(bArr, ":"));
        return bArr;
    }

    public void fillIndex(byte[] bArr, int i) {
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((i >> 8) & 255);
    }

    public void fillCrc(byte[] bArr, int i) {
        int length = bArr.length - 2;
        bArr[length] = (byte) (i & 255);
        bArr[length + 1] = (byte) ((i >> 8) & 255);
    }

    public int crc16(byte[] bArr) {
        int length = bArr.length - 2;
        short[] sArr = {0, -24575};
        int i = 65535;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = bArr[i2];
            for (int i4 = 0; i4 < 8; i4++) {
                i = (sArr[(i ^ i3) & 1] & 65535) ^ (i >> 1);
                i3 >>= 1;
            }
        }
        return i;
    }

    public boolean invalidateProgress() {
        float nextPacketIndex = getNextPacketIndex();
        float f = this.total;
        MyLogger.d("invalidate progress: " + nextPacketIndex + " -- " + f);
        int floor = (int) Math.floor((double) ((nextPacketIndex / f) * 100.0f));
        if (floor == this.progress) {
            return false;
        }
        this.progress = floor;
        return true;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getIndex() {
        return this.index;
    }
}