<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:s="http://www.w3.org/2005/07/scxml">
    <xsl:output method="text" indent="yes"/>



    <xsl:template match="s:scxml">
digraph finite_state_machine {
    size="8,5"
	rank="TD"
        compound = true;
	<xsl:apply-templates select="./s:state"/>
        
        <xsl:for-each select="//s:transition" >
            <xsl:apply-templates select="." />
        </xsl:for-each>
        
}
	</xsl:template>



	<xsl:template match="s:state">
		subgraph "cluster_<xsl:value-of select="./@id"/>" {
			style = "rounded"
                        dummy_<xsl:value-of select="./@id"/> [ label = "", style="invis", shape=point]
			label = "<xsl:value-of select="./@id"/>"
			<xsl:call-template name="substate" />
		}
	</xsl:template>
        
        
        
	<xsl:template match="s:initial">
		"<xsl:value-of select="../@id"/>_init" [ label = "", style="invis", len="0.5",width="0", height="0"];
                {rank=same; <xsl:value-of select="../@id"/>_init cluster_<xsl:value-of select="./@target"/>}
	</xsl:template>



	<xsl:template match="s:final">
		"dummy_<xsl:value-of select="./@id"/>" [label = "<xsl:value-of select="./@id"/>", shape = "doublecircle"]        
        </xsl:template>



	<xsl:template match="s:parallel">
		subgraph "cluster_<xsl:value-of select="./@id"/>" {
			rank = LR;
			label = "parallel_<xsl:value-of select="./@id"/>"
                        dummy_<xsl:value-of select="./@id"/> [ label = "", style="invis", shape=point]
			<xsl:call-template name="substate" />
		}
	</xsl:template>



	<xsl:template match="s:transition">
		<xsl:choose>
			<xsl:when test="name(..) = 'initial'">
                                
				"<xsl:value-of select="../../@id"/><xsl:text>_init" -> </xsl:text>"dummy_<xsl:value-of select="./@target"/>" [label = "", lhead = cluster_<xsl:value-of select="./@target"/>];
                                
			</xsl:when>
                        <!--
                        <xsl:when test="//s:final[@id='./@target']">    
				"dummy_<xsl:value-of select="../@id"/>"<xsl:text> -> </xsl:text>"final_<xsl:value-of select="./@target"/>" [ label = "<xsl:value-of select="./@event"/>" , ltail = cluster_<xsl:value-of select="../@id"/> , lhead = cluster_<xsl:value-of select="./@target"/>];
			</xsl:when>
                        -->
			<xsl:otherwise>
				<xsl:if test="./@target != ''">
					"dummy_<xsl:value-of select="../@id"/>"<xsl:text> -> </xsl:text>"dummy_<xsl:value-of select="./@target"/>" [ label = "<xsl:value-of select="./@event"/>" , ltail = cluster_<xsl:value-of select="../@id"/> , lhead = cluster_<xsl:value-of select="./@target"/>];
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
    
    
        <xsl:template name="substate">
                <xsl:apply-templates select="./s:initial"/>
		<xsl:apply-templates select="./s:state"/>
		<xsl:apply-templates select="./s:parallel"/>
		<xsl:apply-templates select="./s:final"/>
        </xsl:template>
</xsl:stylesheet>

