<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">

    <!-- Identity template -->
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <!-- Inserting enum values from documentation into XSD for easier development process -->

    <xsl:template match="/xs:schema/xs:simpleType[@name='AttributeClassType']/xs:restriction">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xs:enumeration value="trailer_hitch"/>
            <xs:enumeration value="automatic"/>
            <xs:enumeration value="convertible"/>
            <xs:enumeration value="air_condition"/>
            <xs:enumeration value="navigation"/>
            <xs:enumeration value="cruise_control"/>
            <xs:enumeration value="winter_tyres"/>
            <xs:enumeration value="child_seat_0"/>
            <xs:enumeration value="child_seat_1"/>
            <xs:enumeration value="child_seat_4"/>
            <xs:enumeration value="utility"/>
            <xs:enumeration value="doors_4"/>
            <xs:enumeration value="seats_9"/>
            <xs:enumeration value="seats_7"/>
            <xs:enumeration value="seats_5"/>
            <xs:enumeration value="seats_4"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/xs:schema/xs:simpleType[@name='ClassType']/xs:restriction">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xs:enumeration value="bike"/>
            <xs:enumeration value="motorcycle"/>
            <xs:enumeration value="micro"/>
            <xs:enumeration value="mini"/>
            <xs:enumeration value="small"/>
            <xs:enumeration value="medium"/>
            <xs:enumeration value="van"/>
            <xs:enumeration value="transporter"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/xs:schema/xs:simpleType[@name='EngineType']/xs:restriction">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xs:enumeration value="none"/>
            <xs:enumeration value="diesel"/>
            <xs:enumeration value="gasoline"/>
            <xs:enumeration value="electric"/>
            <xs:enumeration value="liquidgas"/>
            <xs:enumeration value="hydrogen"/>
            <xs:enumeration value="hybrid"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/xs:schema/xs:simpleType[@name='ErrorCodeType']/xs:restriction">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xs:enumeration value="auth_provider_unknown"/>
            <xs:enumeration value="auth_invalid_password"/>
            <xs:enumeration value="auth_invalid_token"/>
            <xs:enumeration value="auth_session_invalid"/>
            <xs:enumeration value="auth_anon_not_allowed"/>
            <xs:enumeration value="auth_not_authorized"/>
            <xs:enumeration value="sys_backend_failed"/>
            <xs:enumeration value="sys_unknown_failure"/>
            <xs:enumeration value="sys_not_implemented"/>
            <xs:enumeration value="sys_request_not_plausible"/>
            <xs:enumeration value="booking_target_unknown"/>
            <xs:enumeration value="price_info_not_available"/>
            <xs:enumeration value="booking_too_short"/>
            <xs:enumeration value="booking_too_long"/>
            <xs:enumeration value="booking_target_not_available"/>
            <xs:enumeration value="booking_change_not_possible"/>
            <xs:enumeration value="booking_id_unknown"/>
            <xs:enumeration value="language_not_supported"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/xs:schema/xs:simpleType[@name='ConsumptionClassType']/xs:restriction">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <xs:enumeration value="Time"/>
            <xs:enumeration value="Energy"/>
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>