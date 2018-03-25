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
				<h2 align="center">Exceptions in log file
					<xsl:value-of select="/Records/@log"/>
					on branch <xsl:value-of select="/Records/@branch"/>
				</h2>
				<table class="main" border="2" width = "100%">
					<tr bgcolor="#9acd32">
						<th width = "10%">Time</th>
						<th width = "10%">Type</th>
						<th width = "10%">Name</th>
						<th width = "35%%">StackTrace</th>
						<th width = "35%">PreviousSteps</th>
					</tr>
					<xsl:for-each select="//Record">
						<tr>
							<xsl:variable name="id_gen" select="concat('exception-', position())"/>
							<xsl:variable name="desc" select="Description"/>
							<td class="name"><xsl:value-of select="Time"/></td>
							<td class="name"><xsl:value-of select="Type"/></td>
							<td class="name"><xsl:value-of select="Name"/></td>
							<td class="description">
								<!--<xsl:value-of select="StackTrace"><xsl:text></xsl:text></xsl:value-of>-->
								<a href="javascript:toggleElement('{$id_gen}', 'block')">Click to expand/collapse Stack trace</a>
								<div class="stackTrace" id="{$id_gen}">
									<xsl:value-of select="StackTrace"><xsl:text/></xsl:value-of>
								</div>
							</td>
							<td class="description"><xsl:value-of select="PreviousSteps"/></td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>