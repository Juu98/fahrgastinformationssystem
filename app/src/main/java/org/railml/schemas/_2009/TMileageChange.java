//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:34:29 AM CET 
//


package org.railml.schemas._2009;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tMileageChange complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tMileageChange">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.railml.org/schemas/2009}tElementWithIDAndName">
 *       &lt;attribute name="absPosIn" use="required" type="{http://www.railml.org/schemas/2009}tLengthM" />
 *       &lt;attribute name="absPosInOffset" type="{http://www.railml.org/schemas/2009}tLengthM" />
 *       &lt;attribute name="type" use="required" type="{http://www.railml.org/schemas/2009}tMileageChangeDescr" />
 *       &lt;attribute name="absPos" use="required" type="{http://www.railml.org/schemas/2009}tLengthM" />
 *       &lt;attribute name="pos" use="required" type="{http://www.railml.org/schemas/2009}tLengthM" />
 *       &lt;attribute name="dir" type="{http://www.railml.org/schemas/2009}tDirValidity" />
 *       &lt;anyAttribute namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tMileageChange")
public class TMileageChange
    extends TElementWithIDAndName
{

    @XmlAttribute(name = "absPosIn", required = true)
    protected BigDecimal absPosIn;
    @XmlAttribute(name = "absPosInOffset")
    protected BigDecimal absPosInOffset;
    @XmlAttribute(name = "type", required = true)
    protected TMileageChangeDescr type;
    @XmlAttribute(name = "absPos", required = true)
    protected BigDecimal absPos;
    @XmlAttribute(name = "pos", required = true)
    protected BigDecimal pos;
    @XmlAttribute(name = "dir")
    protected TDirValidity dir;

    /**
     * Gets the value of the absPosIn property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAbsPosIn() {
        return absPosIn;
    }

    /**
     * Sets the value of the absPosIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAbsPosIn(BigDecimal value) {
        this.absPosIn = value;
    }

    /**
     * Gets the value of the absPosInOffset property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAbsPosInOffset() {
        return absPosInOffset;
    }

    /**
     * Sets the value of the absPosInOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAbsPosInOffset(BigDecimal value) {
        this.absPosInOffset = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TMileageChangeDescr }
     *     
     */
    public TMileageChangeDescr getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TMileageChangeDescr }
     *     
     */
    public void setType(TMileageChangeDescr value) {
        this.type = value;
    }

    /**
     * Gets the value of the absPos property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAbsPos() {
        return absPos;
    }

    /**
     * Sets the value of the absPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAbsPos(BigDecimal value) {
        this.absPos = value;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPos() {
        return pos;
    }

    /**
     * Sets the value of the pos property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPos(BigDecimal value) {
        this.pos = value;
    }

    /**
     * Gets the value of the dir property.
     * 
     * @return
     *     possible object is
     *     {@link TDirValidity }
     *     
     */
    public TDirValidity getDir() {
        return dir;
    }

    /**
     * Sets the value of the dir property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDirValidity }
     *     
     */
    public void setDir(TDirValidity value) {
        this.dir = value;
    }

}
