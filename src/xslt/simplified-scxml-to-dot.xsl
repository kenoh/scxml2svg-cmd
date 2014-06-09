<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:s="http://www.w3.org/2005/07/scxml">
    <xsl:output method="text" indent="yes"/>



    <xsl:template match="s:scxml">
digraph finite_state_machine {
    size="8,5"
	rank="TD"
	<xsl:apply-templates select="s:state"/>
        <xsl:apply-templates select="s:parallel"/>
        <xsl:apply-templates select="s:final"/>
        
        <xsl:for-each select="//s:transition" >
            <xsl:apply-templates select="." />
        </xsl:for-each>
        
        <xsl:if test="@initial">
            "ini_<xsl:value-of select="@initial"/>" [ label = "", style="invis", shape=point] ;
            "ini_<xsl:value-of select="@initial"/><xsl:text>" -> </xsl:text>"<xsl:value-of select="./@initial"/>" [label = "", color = "red"];
            {rank=same; ini_<xsl:value-of select="@initial"/><xsl:text> </xsl:text><xsl:value-of select="@initial"/>}
        </xsl:if> 
}
	</xsl:template>



	<xsl:template match="s:state" name="state">
		subgraph "sub_<xsl:value-of select="./@id"/>" {
                        "<xsl:value-of select="./@id"/>";
                        <xsl:text>&#xa;</xsl:text>
                        <xsl:call-template name="substate" />           
		}
	</xsl:template>
        
        
        
        <xsl:template name="substate" >

                 <!--   herer <xsl:value-of select="../s:initial/s:transition/@target"/> herrer -->
                    
                <xsl:if test="name(..) = 'state' and ../s:initial/s:transition/@target != @id"> 
                    <xsl:value-of select="../@id"/><xsl:text> -> </xsl:text><xsl:value-of select="@id"/> [style = "dotted"]
                </xsl:if>
                
                <xsl:if test="name(..) ='parallel'">
                    <xsl:value-of select="../@id"/><xsl:text> -> </xsl:text><xsl:value-of select="@id"/> [color = "blue"]
                </xsl:if>
                
                <xsl:if test="../@initial = @id and name(..) != 'scxml'" >
                    "<xsl:value-of select="../@id"/><xsl:text>" -> </xsl:text>"<xsl:value-of select="@id"/>" [label = "", color = "red"];
                </xsl:if>
               
               <xsl:apply-templates select="s:initial"/>
               <xsl:apply-templates select="s:state"/>
               <xsl:apply-templates select="s:parallel"/>
               <xsl:apply-templates select="s:final"/>
        </xsl:template>
        
        
        
	<xsl:template match="s:initial">
		"<xsl:value-of select="../@id"/>";
                <!-- {rank=same; <xsl:value-of select="../@id"/><xsl:text> </xsl:text><xsl:value-of select="s:transition/@target"/>} -->
                
	</xsl:template>



	<xsl:template match="s:final">
		"<xsl:value-of select="./@id"/>" [shape = "doublecircle"] 
                
                <xsl:call-template name="substate" />  
                <!-- <xsl:apply-template name="substate" />  -->
        </xsl:template>



	<xsl:template match="s:parallel">
			<xsl:call-template name="state" />
	</xsl:template>



	<xsl:template match="s:transition">
		<xsl:choose>
			<xsl:when test="name(..) = 'initial'">
                                
				"<xsl:value-of select="../../@id"/><xsl:text>" -> </xsl:text>"<xsl:value-of select="./@target"/>" [label = "", color = "red"];
                                
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="./@target != ''">
					"<xsl:value-of select="../@id"/>"<xsl:text> -> </xsl:text>"<xsl:value-of select="./@target"/>" [ label = "<xsl:value-of select="./@event"/> <xsl:value-of select="./@cond"/>" ];
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
   
</xsl:stylesheet>

