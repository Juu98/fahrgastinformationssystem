//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:34:29 AM CET 
//


package org.railml.schemas._2009;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eGear complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eGear">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.railml.org/schemas/2009}tGear">
 *       &lt;sequence>
 *         &lt;element name="efficiency" type="{http://www.railml.org/schemas/2009}tEfficiencyCurve" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eGear", propOrder = {
    "efficiency"
})
public class EGear
    extends TGear
{

    protected TEfficiencyCurve efficiency;

    /**
     * Gets the value of the efficiency property.
     * 
     * @return
     *     possible object is
     *     {@link TEfficiencyCurve }
     *     
     */
    public TEfficiencyCurve getEfficiency() {
        return efficiency;
    }

    /**
     * Sets the value of the efficiency property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEfficiencyCurve }
     *     
     */
    public void setEfficiency(TEfficiencyCurve value) {
        this.efficiency = value;
    }

}
