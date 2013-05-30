<!DOCTYPE stylesheet [
  <!ENTITY cr "<xsl:text>
</xsl:text>">
]>

    <xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
        xmlns:xalan="http://xml.apache.org/xslt" 
        version="1.0">

        <xsl:output method="xml" indent="yes" xalan:indent-amount="3"/> 

        <!-- copy out the xml -->
        <xsl:template match="* | @*">
            <xsl:copy><xsl:copy-of select="@*"/><xsl:apply-templates/></xsl:copy>
        </xsl:template>

    </xsl:stylesheet>