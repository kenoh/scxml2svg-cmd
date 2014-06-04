<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:s="http://www.w3.org/2005/07/scxml">
    <xsl:output method="text" indent="yes"/>



    <xsl:template match="s:scxml">
digraph finite_state_machine {
    size="8,5"
	rank="TD"
	<xsl:apply-templates select="./s:state"/>
        
        <xsl:for-each select="//s:transition" >
            <xsl:apply-templates select="." />
        </xsl:for-each>
        
}
	</xsl:template>



	<xsl:template match="s:state">
		subgraph "sub_<xsl:value-of select="./@id"/>" {
                        <xsl:value-of select="./@id"/>
                        <xsl:text>&#xa;</xsl:text>
			<xsl:call-template name="substate" />
                         
		}
	</xsl:template>
        
        
        
	<xsl:template match="s:initial">
		"<xsl:value-of select="../@id"/>";
                {rank=same; <xsl:value-of select="../@id"/> <xsl:value-of select="./@target"/>}
	</xsl:template>



	<xsl:template match="s:final">
		"<xsl:value-of select="./@id"/>" [shape = "doublecircle"]    
                <xsl:call-template name="substate" />    
        </xsl:template>



	<xsl:template match="s:parallel">
		subgraph "cluster_<xsl:value-of select="./@id"/>" {
			rank = LR;
			label = "parallel_<xsl:value-of select="./@id"/>"
                        "<xsl:value-of select="./@id"/>"
                        <xsl:text>&#xa;</xsl:text>
			<xsl:call-template name="substate" />
		}
	</xsl:template>



	<xsl:template match="s:transition">
		<xsl:choose>
			<xsl:when test="name(..) = 'initial'">
                                
				"<xsl:value-of select="../../@id"/><xsl:text>" -> </xsl:text>"<xsl:value-of select="./@target"/>" [label = "", color = "red"];
                                
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="./@target != ''">
					"<xsl:value-of select="../@id"/>"<xsl:text> -> </xsl:text>"<xsl:value-of select="./@target"/>" [ label = "<xsl:value-of select="./@event"/>" ];
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
    
    
        <xsl:template name="substate">          
                <!-- <xsl:if test="../initial/transition/@target != ./@id"> -->
                    <xsl:value-of select="../@id"/><xsl:text> -> </xsl:text><xsl:value-of select="./@id"/> [style = "dotted"]
               
                <xsl:apply-templates select="./s:initial"/>
		<xsl:apply-templates select="./s:state"/>
		<xsl:apply-templates select="./s:parallel"/>
		<xsl:apply-templates select="./s:final"/>
        </xsl:template>
</xsl:stylesheet>

