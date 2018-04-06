<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<html>
			<script type="text/javascript" src="reportng.js"/>
			<style>
				#className {padding: 2px; text-align: center; font-size: 1.2em; font-family: courier; color: darkslateblue}
				td.name {padding: 2px; vertical-align: center; text-align: center; }
				td.description {padding: 2px; vertical-align: top; text-align: left; }
			</style>
			<body>
				<!-- probably add buildName/buildNumber and time stamp to builds attributes
                    <h2 align="center">Exceptions in log file
                        <xsl:value-of select="/Builds/@log"/>
                        on branch <xsl:value-of select="/Builds/@branch"/>
                    </h2> -->
				<h2 align="center">Build with downstream jobs consolidated information</h2>
				<table class="main" border="2" width = "100%">
					<tr bgcolor="#9acd32">
						<th width = "10%">JobName</th>
						<th width = "10%">DisplayName</th>
						<th width = "5%">Number</th>
						<th width = "15%%">Parameters</th>
						<th width = "5%">LogSize, KB</th>
						<th width = "5%">BuildTime, sec</th>						
						<th width = "5%">BuildResult</th>
						<th width = "10%">PathToResult</th>
						<th width = "35%">StackTrace</th>
					</tr>
					<xsl:for-each select="//Build">
						<tr>
							<xsl:variable name="id_gen" select="concat('exception-', position())"/>
							<xsl:variable name="desc" select="Description"/>
							<td class="name"><xsl:value-of select="JobName"/></td>
							<td class="name"><xsl:value-of select="Name"/></td>
							<td class="name"><xsl:value-of select="Number"/></td>
							<td class="description"><xsl:value-of select="Parameters"/></td>
							<td class="name"><xsl:value-of select="LogSize"/></td>
							<td class="name"><xsl:value-of select="BuildTime"/></td>
							<td class="name"><xsl:value-of select="BuildResult"/></td>
							<td class="name"><xsl:value-of select="PathToResult"/></td>
							<td class="description">
								<!--<xsl:value-of select="StackTrace"><xsl:text></xsl:text></xsl:value-of>-->
								<a href="javascript:toggleElement('{$id_gen}', 'block')">Click to expand/collapse Stack trace</a>
								<div class="stackTrace" id="{$id_gen}">
									<xsl:value-of select="StackTrace"><xsl:text/></xsl:value-of>
								</div>
							</td>
							<!--<td class="description"><xsl:value-of select="PreviousSteps"/></td>-->
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>
