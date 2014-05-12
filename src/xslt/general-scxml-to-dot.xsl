<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:s="http://www.w3.org/2005/07/scxml">
    <xsl:output method="text" indent="yes"/>



    <xsl:template match="s:scxml">
digraph finite_state_machine {
    size="8,5"
	rank="TD"
	<xsl:apply-templates select="./s:state"/>
}
	</xsl:template>



	<xsl:template match="s:state">
		subgraph "cluster_<xsl:value-of select="./@id"/>" {
			style = "rounded"
			label = "<xsl:value-of select="./@id"/>"
			<xsl:apply-templates select="./s:final"/>
			<xsl:apply-templates select="./s:initial"/>
			<xsl:apply-templates select="./s:transition"/>
			<xsl:apply-templates select="./s:state"/>
			<xsl:apply-templates select="./s:paralel"/>
		}
	</xsl:template>



	<xsl:template match="s:initial">
		<xsl:apply-templates select="./s:transition"/>
	</xsl:template>



	<xsl:template match="s:final">
		"cluster_<xsl:value-of select="./@id"/>" [ shape="doublecircle" ];
    </xsl:template>



	<xsl:template match="s:paralel">
		subgraph "cluster_<xsl:value-of select="./@id"/>" {
			rank = LR;
			label = "<xsl:value-of select="./@id"/>"
			<xsl:apply-templates select="./s:initial"/>
			<xsl:apply-templates select="./s:state"/>
			<xsl:apply-templates select="./s:paralel"/>
			<xsl:apply-templates select="./s:final"/>
		}
	</xsl:template>



	<xsl:template match="s:transition">
		<xsl:choose>
			<xsl:when test="name(..) = 'initial'">
				"<xsl:value-of select="../../@id"/>_init" [ label = "", style="invis", len="0.5",width="0", height="0"];
				"<xsl:value-of select="../../@id"/><xsl:text>_init" -> </xsl:text>"cluster_<xsl:value-of select="./@target"/>" [label = ""];
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="./@target != ''">
					"cluster_<xsl:value-of select="../@id"/>"<xsl:text> -> </xsl:text>"cluster_<xsl:value-of select="./@target"/>" [ label = "<xsl:value-of select="./@event"/>" ];
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
</xsl:stylesheet>

