<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
    <xsl:param name="versionParam" select="'1.0'"/>

    <xsl:template match="letter">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="first"
                                       page-height="297mm" page-width="210mm"
                                       margin-top="10mm" margin-bottom="10mm"
                                       margin-left="20mm" margin-right="10mm">
                    <fo:region-body margin-left="25mm" margin-right="20mm"/>
                    <fo:region-after region-name="footer-first" extent="20mm"/>
                </fo:simple-page-master>
                <fo:simple-page-master master-name="rest"
                                       page-height="297mm" page-width="210mm"
                                       margin-top="10mm" margin-bottom="10mm"
                                       margin-left="20mm" margin-right="10mm">
                    <fo:region-body margin-left="25mm" margin-right="20mm"/>
                    <fo:region-after region-name="footer-rest" extent="20mm"/>
                </fo:simple-page-master>
                <fo:page-sequence-master master-name="document">
                    <fo:repeatable-page-master-alternatives>
                        <fo:conditional-page-master-reference page-position="first"
                                                              master-reference="first"/>
                        <fo:conditional-page-master-reference page-position="rest"
                                                              master-reference="rest"/>
                    </fo:repeatable-page-master-alternatives>
                </fo:page-sequence-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="document">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" margin-left="5mm" border-collapse="separate" border="solid 1pt black">
                            <fo:table-column column-width="80mm"/>
                            <fo:table-body>
                                <xsl:apply-templates select="recipient"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block font-size="12pt">
                        <xsl:value-of select="textblock" />
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

<!-- ========================= -->
<!-- child element: recipient  -->
<!-- ========================= -->
    <xsl:template match="recipient">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="person">
        <fo:table-row>
            <fo:table-cell>
                <fo:block>
                    <fo:block white-space-collapse="false">
                        <xsl:value-of select="firstname"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="lastname"/>
                    </fo:block>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="streetAndHousenumber"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell>
                <fo:block white-space-collapse="false">
                    <xsl:value-of select="postcode"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="city"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>



    <!--xsl:template match="notiz">
        <xsl:processing-instruction name="cocoon-format">type="text/xslfo"</xsl:processing-instruction>

        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master
                    page-master-name="one"
                    page-height="15cm"
                    page-width="21cm"
                    margin-left="2cm"
                    margin-right="2cm">
                    <fo:region-body margin-top="50pt" margin-bottom="50pt"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence>
                <fo:sequence-specification>
                    <fo:sequence-specifier-repeating page-master-first="one" page-master-repeating="one"/>
                </fo:sequence-specification>

                <fo:flow font-size="14pt" line-height="18pt">

                    <xsl:apply-templates select="titel"/>

                    <fo:table space-before.optimum="6pt">
                        <fo:table-column column-width="2cm"/>
                        <fo:table-column column-width="10cm"/>
                        <fo:table-body>
                            <xsl:apply-templates select="autor|datum|absatz"/>
                        </fo:table-body>
                    </fo:table>

                    <fo:block text-align="justified"
                              space-before.optimum="30pt">
                        Copyright © 2000 Stefan Mintert. Dieses Beispiel stammt aus der
                        zweiten Auflage von »XML in der
                        Praxis«.
                        <fo:inline font-style="italic">http://www.mintert.com/xml/</fo:inline>
                    </fo:block>


                </fo:flow>

            </fo:page-sequence>

        </fo:root>

    </xsl:template>





    <xsl:template match="titel">
        <fo:block text-align="centered"
                  font-size="24pt"
                  font-weight="bold"
                  line-height="28pt"><xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <xsl:template match="autor">
        <fo:table-row space-before.optimum="6pt">
            <fo:table-cell>
                <fo:block text-align="right" font-style="italic">von</fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block text-align="left"><xsl:apply-templates/></fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="datum">
        <fo:table-row space-before.optimum="6pt">
            <fo:table-cell>
                <fo:block text-align="right" font-style="italic">für den</fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block text-align="left"><xsl:apply-templates/></fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="restzeit">
        <fo:table-row space-before.optimum="6pt">
            <fo:table-cell>
                <fo:block text-align="right" font-style="italic">in</fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block text-align="left"><xsl:apply-templates/></fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="absatz">
        <fo:table-row space-before.optimum="6pt">
            <fo:table-cell>
                <fo:block text-align="right"></fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block text-align="left"
                          space-after.optimum="6pt"><xsl:apply-templates/></fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <xsl:template match="wichtig">
        <fo:inline-sequence font-weight="bold">
            <xsl:apply-templates/>
        </fo:inline-sequence>
    </xsl:template>


</xsl:stylesheet>





<?xml version="1.0"?>
<fo:root- xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="297mm" page-width="210mm"
                               margin-top="10mm" margin-bottom="10mm"
                               margin-left="20mm" margin-right="10mm">
            <fo:region-body margin-left="25mm" margin-right="20mm"/>
            <fo:region-after region-name="footer-first" extent="20mm"/>
        </fo:simple-page-master>
        <fo:simple-page-master master-name="rest"
                               page-height="297mm" page-width="210mm"
                               margin-top="10mm" margin-bottom="10mm"
                               margin-left="20mm" margin-right="10mm">
            <fo:region-body margin-left="25mm" margin-right="20mm"/>
            <fo:region-after region-name="footer-rest" extent="20mm"/>
        </fo:simple-page-master>
        <fo:page-sequence-master master-name="document">
            <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference page-position="first"
                                                      master-reference="first"/>
                <fo:conditional-page-master-reference page-position="rest"
                                                      master-reference="rest"/>
            </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>
    </fo:layout-master-set>
    <fo:page-sequence master-reference="document">
        <fo:static-content flow-name="footer-first">
            <fo:block text-align="center">First page. {$t1}</fo:block>
        </fo:static-content>
        <fo:static-content flow-name="footer-rest">
            <fo:block text-align-last="center">Other page.</fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
            <fo:block/>
            <fo:block break-before="page"/>
            <fo:block break-before="page"/>
        </fo:flow>
    </fo:page-sequence>
</fo:root-->
