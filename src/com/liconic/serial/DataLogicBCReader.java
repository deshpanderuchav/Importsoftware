package com.liconic.serial;

import gnu.io.CommDriver;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.*;
import com.liconic.stages.ImportStage;
import org.apache.logging.log4j.Logger;

public class DataLogicBCReader implements SerialPortEventListener {

    private SerialPort serialPort;
    private CommPortIdentifier portId;

    private OutputStream outputStream;
    private InputStream inputStream;

    private String PortName;

    private ImportStage IS;
    private Logger log;

    public DataLogicBCReader(String PortName, ImportStage IS, Logger log) throws Exception {

        this.IS = IS;
        this.PortName = PortName;
        this.log = log;

        OpenPort();
    }

    public void serialEvent(SerialPortEvent event) {

        int numBytes = 0;

        switch (event.getEventType()) {
            case SerialPortEvent.BI:
                System.out.println("SerialPortEvent.BI occurred");
            case SerialPortEvent.OE:
                System.out.println("SerialPortEvent.OE occurred");
            case SerialPortEvent.FE:
                System.out.println("SerialPortEvent.FE occurred");
            case SerialPortEvent.PE:
                System.out.println("SerialPortEvent.PE occurred");
            case SerialPortEvent.CD:
                System.out.println("SerialPortEvent.CD occurred");
            case SerialPortEvent.CTS:
                System.out.println("SerialPortEvent.CTS occurred");
            case SerialPortEvent.DSR:
                System.out.println("SerialPortEvent.DSR occurred");
            case SerialPortEvent.RI:
                System.out.println("SerialPortEvent.RI occurred");
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("SerialPortEvent.OUTPUT_BUFFER_EMPTY occurred");
                break;
            case SerialPortEvent.DATA_AVAILABLE:

                System.out.println("SerialPortEvent.DATA_AVAILABLE occurred");

                byte[] readBuffer = new byte[20];

                try {

                    while (inputStream.available() > 0) {

                        try {
                            Thread.sleep(100);
                        } catch (Exception E) {
                        }

                        numBytes = inputStream.read(readBuffer);

                        System.out.println("Read = " + numBytes);

                    }

                    if (numBytes > 1) {
                        ReadedBarcode(new String(readBuffer));
                    }

                } catch (IOException ioe) {
                    System.out.println("Exception " + ioe);
                    log.error("Barcode Reader event: " + ioe.getMessage());
                }
                break;
        }
    }

    private void ReadedBarcode(String BCR) {

        System.out.println("String Length=" + BCR.length() + " " + BCR);

        log.info("Barcode read: " + BCR);

        if (BCR.contains("Two-Ways Out of Sequence!")) {
            return;
        }

        if (BCR.indexOf('\r') == -1) {
            return;
        }

        BCR = BCR.substring(0, BCR.indexOf('\r'));

        if (BCR.length() == 1024) {
            return;
        }

        String MSG = "";

        char c1 = 13;
        char c2 = 27;

        if (IS.CheckBarcode(BCR)) {

            MSG = c2 + "[0q" + c1;

        } else {

            MSG = c2 + "[4q" + c1;
        }

    }

    private void OpenPort() {
        try {

            portId = CommPortIdentifier.getPortIdentifier(PortName);

            serialPort = (SerialPort) portId.open("DataLogic", 2000);

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            serialPort.addEventListener(this);

            serialPort.notifyOnDataAvailable(true);

            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        } catch (Exception Ex) {
            log.error("Barcode open port: " + Ex.getMessage());
        }
    }

    public void ClosePort() {
        try {
            serialPort.close();
        } catch (Exception E) {
            log.error("Barcode close port: " + E.getMessage());
        }
    }

}
