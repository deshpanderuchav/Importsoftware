//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.21 at 12:23:44 AM GST 
//


package com.liconic.binding.sys;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for STXTubePosType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="STXTubePosType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PX" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PY" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PYA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PTTV" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "STXTubePosType", propOrder = {
    "id",
    "px",
    "py",
    "pya",
    "pttv"
})
public class STXTubePosType {

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "PX")
    protected int px;
    @XmlElement(name = "PY")
    protected int py;
    @XmlElement(name = "PYA", required = true)
    protected String pya;
    @XmlElement(name = "PTTV")
    protected int pttv;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the px property.
     * 
     */
    public int getPX() {
        return px;
    }

    /**
     * Sets the value of the px property.
     * 
     */
    public void setPX(int value) {
        this.px = value;
    }

    /**
     * Gets the value of the py property.
     * 
     */
    public int getPY() {
        return py;
    }

    /**
     * Sets the value of the py property.
     * 
     */
    public void setPY(int value) {
        this.py = value;
    }

    /**
     * Gets the value of the pya property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPYA() {
        return pya;
    }

    /**
     * Sets the value of the pya property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPYA(String value) {
        this.pya = value;
    }

    /**
     * Gets the value of the pttv property.
     * 
     */
    public int getPTTV() {
        return pttv;
    }

    /**
     * Sets the value of the pttv property.
     * 
     */
    public void setPTTV(int value) {
        this.pttv = value;
    }

}
