<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:s="http://www.w3.org/2005/07/scxml">    
    <xsl:output method="text" indent="yes"/>
    
    <xsl:template match="s:scxml">
digraph finite_state_machine {
        size="8,5"
        <xsl:if test="//s:final">node [shape = doublecircle]; [<xsl:apply-templates select="//s:final"/>]</xsl:if>
        node [shape = circle];
        <xsl:apply-templates select="//s:state/s:transition"/>
        <xsl:apply-templates select="//s:paralel/s:transition"/>
}        
    </xsl:template>
    <xsl:template match="s:final">
        <xsl:value-of select="./@id"/>
    </xsl:template>
    
    <xsl:template match="s:transition">
        <xsl:value-of select="../@id"/><xsl:text> -> </xsl:text><xsl:value-of select="./@target"/> [ label = <xsl:value-of select="./@event"/> ]
    </xsl:template>
</xsl:stylesheet>
