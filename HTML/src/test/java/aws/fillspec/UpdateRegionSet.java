package aws.fillspec;

import java.util.Base64;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Generated values of update regions for product market
 *
 * @since Akela_UR_Splitting_V11_18.xlsx
 */
public enum UpdateRegionSet
{
  UNDEFINED(CountryCodeNames.UNDEFINED, "UNDEFINED", ImmutableSet.of(-1), ImmutableSet.of(-1), ProductMarket.UNDEFINED),
  CHL_CHL(CountryCodeNames.CHL, "Q2hpbGUgTm9ydGg=", ImmutableSet.of(27300), ImmutableSet.of(27301, 27302), ProductMarket.CHL),
  TUR_TUR(CountryCodeNames.TUR, "VHVya2V5IE5vcnRoZWFzdCAoVDUp", ImmutableSet.of(27200), ImmutableSet.of(27201, 27202, 27203, 27204, 27205, 27206, 27207), ProductMarket.TUR),
  ISR_ISR(CountryCodeNames.ISR, "SXNyYWVs", ImmutableSet.of(27100), ImmutableSet.of(27101), ProductMarket.ISR),
  AUS_AUS(CountryCodeNames.AUS, "U291dGggQXVzdHJhbGlh", ImmutableSet.of(23600), ImmutableSet.of(23601, 23602, 23603, 23604, 23605), ProductMarket.AUS),
  NAR_VIR(CountryCodeNames.VIR, "QmFoYW1hcytDYXltYW4gSXNsYW5kcytQdWVydG8gUmljbytVUyBWaXJnaW4gSXNsYW5kcw==", ImmutableSet.of(16900), ImmutableSet.of(16901), ProductMarket.NAR),
  EUR_GIB(CountryCodeNames.GIB, "U3BhaW4gV2VzdGVybiBBbmRhbHVzaWEgKyBHaWJyYWx0YXIgKEsyKQ==", ImmutableSet.of(10600), ImmutableSet.of(10615), ProductMarket.EUR),
  EUR_AUT(CountryCodeNames.AUT, "QXVzdHJpYSBDZW50ZXJhbCAoQTEp", ImmutableSet.of(10100), ImmutableSet.of(10101, 10102, 10103), ProductMarket.EUR),
  SAM_CRI(CountryCodeNames.CRI, "UGFuYW1hICsgQ29zdGEgUmljYQ==", ImmutableSet.of(22900), ImmutableSet.of(22901), ProductMarket.SAM),
  EUR_IMN(CountryCodeNames.IMN, "RW5nbGFuZCBOb3J0aCArIElzbGUgb2YgTWFuIChFMyk=", ImmutableSet.of(10900), ImmutableSet.of(10907), ProductMarket.EUR),
  EUR_CZE(CountryCodeNames.CZE, "Q3plY2ggUmVwdWJsaWMgRWFzdCAoQzIp", ImmutableSet.of(12200), ImmutableSet.of(12201, 12202), ProductMarket.EUR),
  EUR_BGR(CountryCodeNames.BGR, "TWFjZWRvbmlhICsgQnVsZ2FyaWEgKE00K0g3KQ==", ImmutableSet.of(11800), ImmutableSet.of(11801), ProductMarket.EUR),
  EUR_NOR(CountryCodeNames.NOR, "Tm9yd2F5IFdlc3Rlcm4gICsgU291dGhlcm4gKE4xKQ==", ImmutableSet.of(11300), ImmutableSet.of(11301, 11302, 11303, 11304), ProductMarket.EUR),
  EUR_AND(CountryCodeNames.AND, "RnJhbmNlIFNvdXRod2VzdCArIEFuZG9ycmEgKEY1KQ==", ImmutableSet.of(10800), ImmutableSet.of(10822), ProductMarket.EUR),
  EUR_ROU(CountryCodeNames.ROU, "Um9tYW5pYSAoSDgp", ImmutableSet.of(13300), ImmutableSet.of(13301), ProductMarket.EUR),
  EUR_GRC(CountryCodeNames.GRC, "R3JlZWNlIE1hY2Vkb25pYSAoR0sp", ImmutableSet.of(12400), ImmutableSet.of(12401, 12402, 12403, 12404), ProductMarket.EUR),
  EUR_CHE(CountryCodeNames.CHE, "U3dpdHplcmxhbmQgRWFzdCArIExpZWNodGVuc3RlaW4gKENBKQ==", ImmutableSet.of(10300), ImmutableSet.of(10301, 10302), ProductMarket.EUR),
  WOM_TUR(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5012), ProductMarket.WOM_TUR),
  SAM_PRY(CountryCodeNames.PRY, "UGFyYWd1YXkgKyBVcnVndWF5", ImmutableSet.of(22500), ImmutableSet.of(22501), ProductMarket.SAM),
  EUR_MDA(CountryCodeNames.MDA, "VWtyYWluZSBOb3J0aHdlc3QgKyBNb2xkb3ZhIChVMStVNCtIOSk=", ImmutableSet.of(14000), ImmutableSet.of(14002), ProductMarket.EUR),
  AGCC_OMN(CountryCodeNames.OMN, "RW1pcmF0ZXMgKyBPbWFu", ImmutableSet.of(24900), ImmutableSet.of(24901), ProductMarket.AGCC),
  ASIA_BRN(CountryCodeNames.BRN, "TWFsYXlzaWE6U2luZ2Fwb3JlOkJydW5laQ==", ImmutableSet.of(24200), ImmutableSet.of(24202), ProductMarket.ASIA),
  WOM_CHILE(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5002), ProductMarket.WOM_CHILE),
  AGCC_ARE(CountryCodeNames.ARE, "RW1pcmF0ZXMgKyBPbWFu", ImmutableSet.of(24900), ImmutableSet.of(24901), ProductMarket.AGCC),
  EUR_HRV(CountryCodeNames.HRV, "Q3JvYXRpYSAoSDIp", ImmutableSet.of(12500), ImmutableSet.of(12501), ProductMarket.EUR),
  EUR_PRT(CountryCodeNames.PRT, "UG9ydHVnYWwgTm9ydGggKEs4KQ==", ImmutableSet.of(11400), ImmutableSet.of(11401, 11402), ProductMarket.EUR),
  NAR_BHS(CountryCodeNames.BHS, "QmFoYW1hcytDYXltYW4gSXNsYW5kcytQdWVydG8gUmljbytVUyBWaXJnaW4gSXNsYW5kcw==", ImmutableSet.of(16900), ImmutableSet.of(16901), ProductMarket.NAR),
  SAM_MTQ(CountryCodeNames.MTQ, "VmVuZXp1ZWxhICsgR3VhZGVsb3VwZSArIE1hcnRpbmlxdWU=", ImmutableSet.of(22600), ImmutableSet.of(22601), ProductMarket.SAM),
  EUR_MLT(CountryCodeNames.MLT, "SXRhbHkgU2ljaWxpYSAgKyBNYWx0YQ==", ImmutableSet.of(11100), ImmutableSet.of(11111), ProductMarket.EUR),
  WOM_NAR(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5004), ProductMarket.WOM_NAR),
  EUR_CYP(CountryCodeNames.CYP, "Tm9ydGhlcm4gQ3lwcnVzIERB", ImmutableSet.of(12100), ImmutableSet.of(12101, 12103), ProductMarket.EUR),
  SA_SWZ(CountryCodeNames.SWZ, "U3dhemlsYW5kK1pBRihHYXV0ZW5nK01wdW1hbGFuZ2Ep", ImmutableSet.of(26500), ImmutableSet.of(26501), ProductMarket.SA),
  WOM_NA(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5010), ProductMarket.WOM_NA),
  WOM_EU(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5001), ProductMarket.WOM_EU),
  NAR_MEX(CountryCodeNames.MEX, "TWV4aWNvIDY=", ImmutableSet.of(16700), ImmutableSet.of(16704, 16705, 16706, 16701, 16702, 16703), ProductMarket.NAR),
  ASIA_VNM(CountryCodeNames.VNM, "VmlldG5hbQ==", ImmutableSet.of(24000), ImmutableSet.of(24001, 24002), ProductMarket.ASIA),
  SAM_PER(CountryCodeNames.PER, "UGVydQ==", ImmutableSet.of(22700), ImmutableSet.of(22701), ProductMarket.SAM),
  EUR_HUN(CountryCodeNames.HUN, "SHVuZ2FyeSAoSDEp", ImmutableSet.of(12600), ImmutableSet.of(12601), ProductMarket.EUR),
  EUR_BIH(CountryCodeNames.BIH, "Qm9zbmlhIGFuZCBIZXJ6ZWdvdmluYSAoTTYp", ImmutableSet.of(11900), ImmutableSet.of(11901), ProductMarket.EUR),
  NA_EGY(CountryCodeNames.EGY, "RWd5cHQ=", ImmutableSet.of(25800), ImmutableSet.of(25801, 25802, 25803), ProductMarket.NA),
  ASIA_IDN(CountryCodeNames.IDN, "SW5kb25lc2lh", ImmutableSet.of(23900), ImmutableSet.of(23904, 23905, 23901, 23902, 23903), ProductMarket.ASIA),
  EUR_FIN(CountryCodeNames.FIN, "RmlubGFuZCBOb3JyYSBGaW5sYW5kIChTVik=", ImmutableSet.of(10700), ImmutableSet.of(10704, 10705, 10706, 10707, 10708, 10709, 10701, 10702, 10703), ProductMarket.EUR),
  EUR_LUX(CountryCodeNames.LUX, "QmVsZ2l1bSBOb3J0aCArIEx1eGVtYm91cmcgKEJCKQ==", ImmutableSet.of(10200), ImmutableSet.of(10202), ProductMarket.EUR),
  SAM_BRA(CountryCodeNames.BRA, "U2FudGEgQ2F0YXJpbmE=", ImmutableSet.of(22300), ImmutableSet.of(22304, 22305, 22306, 22307, 22308, 22309, 22310, 22311, 22301, 22302, 22303), ProductMarket.SAM),
  NAR_CAN(CountryCodeNames.CAN, "UXXDqWJlY18z", ImmutableSet.of(16600), ImmutableSet.of(16608, 16609, 16610, 16611, 16601, 16602, 16603, 16604, 16605, 16606, 16607), ProductMarket.NAR),
  EUR_TUR(CountryCodeNames.TUR, "VHVya2V5IE5vcnRoZWFzdCAoVDUp", ImmutableSet.of(13900), ImmutableSet.of(13904, 13905, 13906, 13907, 13901, 13902, 13903), ProductMarket.EUR),
  EUR_LVA(CountryCodeNames.LVA, "TGF0dmlhIChINSk=", ImmutableSet.of(13100), ImmutableSet.of(13101), ProductMarket.EUR),
  EUR_DEU(CountryCodeNames.DEU, "R2VybWFueSBTb3V0aGVhc3QgKEc4KQ==", ImmutableSet.of(10400), ImmutableSet.of(10401, 10402, 10403, 10404, 10405, 10406, 10407, 10408, 10409, 10410, 10411, 10412, 10413), ProductMarket.EUR),
  EUR_FRA(CountryCodeNames.FRA, "RnJhbmNlIFByb3ZlbmNlLUFscGVzLUPDtHRlIGQnQXp1ciArIE1vbmFjbyAoRjMp", ImmutableSet.of(10800), ImmutableSet.of(10816, 10817, 10818, 10819, 10820, 10821, 10822, 10823, 10801, 10802, 10803, 10804, 10805, 10806, 10807, 10808, 10809, 10810, 10811, 10812, 10813, 10814, 10815), ProductMarket.EUR),
  AGCC_QAT(CountryCodeNames.QAT, "QmFocmFpbiArIFF1YXRhciArIEt1d2FpdA==", ImmutableSet.of(25100), ImmutableSet.of(25101), ProductMarket.AGCC),
  NA_TUN(CountryCodeNames.TUN, "VHVuaXNpYQ==", ImmutableSet.of(26100), ImmutableSet.of(26101), ProductMarket.NA),
  EUR_SRB(CountryCodeNames.SRB, "U2VyYmlhIENlbnRyYWwgKyBWb2p2b2RpbmEgKE01KQ==", ImmutableSet.of(13600), ImmutableSet.of(13601, 13602), ProductMarket.EUR),
  EUR_MNE(CountryCodeNames.MNE, "QWxiYW5pYSArIE1vbnRlbmVncm8gKE0zK01LKQ==", ImmutableSet.of(11600), ImmutableSet.of(11601), ProductMarket.EUR),
  NAR_CYM(CountryCodeNames.CYM, "QmFoYW1hcytDYXltYW4gSXNsYW5kcytQdWVydG8gUmljbytVUyBWaXJnaW4gSXNsYW5kcw==", ImmutableSet.of(16900), ImmutableSet.of(16901), ProductMarket.NAR),
  EUR_LTU(CountryCodeNames.LTU, "TGl0aHVhbmlhIChINik=", ImmutableSet.of(13000), ImmutableSet.of(13001), ProductMarket.EUR),
  AGCC_KWT(CountryCodeNames.KWT, "QmFocmFpbiArIFF1YXRhciArIEt1d2FpdA==", ImmutableSet.of(25100), ImmutableSet.of(25101), ProductMarket.AGCC),
  WOM_ASIA(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5006), ProductMarket.WOM_ASIA),
  EUR_SJM(CountryCodeNames.SJM, "SWNlbGFuZCArIEbDuHJveWFyICsgU3ZhbGJhcmQgKE4yK0ZPK1NKKQ==", ImmutableSet.of(11000), ImmutableSet.of(11001), ProductMarket.EUR),
  EUR_GBR(CountryCodeNames.GBR, "SXJlbGFuZCArIE5vcnRoZXJuIElyZWxhbmQgKEU3K0U0KQ==", ImmutableSet.of(10900), ImmutableSet.of(10901, 10902, 10903, 10904, 10905, 10906, 10907, 10908), ProductMarket.EUR),
  NAR_PRI(CountryCodeNames.PRI, "QmFoYW1hcytDYXltYW4gSXNsYW5kcytQdWVydG8gUmljbytVUyBWaXJnaW4gSXNsYW5kcw==", ImmutableSet.of(16900), ImmutableSet.of(16901), ProductMarket.NAR),
  SAM_MEX(CountryCodeNames.MEX, "TWV4aWNvIDY=", ImmutableSet.of(23000), ImmutableSet.of(23001, 23002, 23003, 23004, 23005, 23006), ProductMarket.SAM),
  INDIA_IND(CountryCodeNames.IND, "RGFtYW4gJiBEaXU=", ImmutableSet.of(24500), ImmutableSet.of(24512, 24513, 24514, 24515, 24516, 24517, 24518, 24519, 24520, 24521, 24522, 24523, 24524, 24525, 24526, 24527, 24528, 24529, 24530, 24531, 24501, 24502, 24503, 24504, 24505, 24506, 24507, 24508, 24509, 24510, 24511), ProductMarket.INDIA),
  AGCC_LBN(CountryCodeNames.LBN, "TGliYW5vbiArIEpvcmRhbg==", ImmutableSet.of(25000), ImmutableSet.of(25001), ProductMarket.AGCC),
  ASIA_PHL(CountryCodeNames.PHL, "UGhpbGlwaW5lcw==", ImmutableSet.of(24100), ImmutableSet.of(24101, 24102), ProductMarket.ASIA),
  SAM_ARG(CountryCodeNames.ARG, "Tm9ydGggQXJnZW50aW5h", ImmutableSet.of(22200), ImmutableSet.of(22201, 22202, 22203), ProductMarket.SAM),
  NA_MAR(CountryCodeNames.MAR, "TW9yb2Njbw==", ImmutableSet.of(26000), ImmutableSet.of(26001), ProductMarket.NA),
  EUR_POL(CountryCodeNames.POL, "UG9sYW5kIFNvdXRoIChQMyk=", ImmutableSet.of(13400), ImmutableSet.of(13401, 13402, 13403), ProductMarket.EUR),
  ASIA_THA(CountryCodeNames.THA, "VGhhaWxhbmQ=", ImmutableSet.of(23800), ImmutableSet.of(23801, 23802, 23803, 23804, 23805, 23806), ProductMarket.ASIA),
  EUR_SMR(CountryCodeNames.SMR, "SXRhbHkgQ2VudHJhbCArIFNhbiBNYXJpbm8gKEkyKQ==", ImmutableSet.of(11100), ImmutableSet.of(11112), ProductMarket.EUR),
  NAR_USA(CountryCodeNames.USA, "V2lzY29uc2lu", ImmutableSet.of(16800), ImmutableSet.of(16801, 16802, 16803, 16804, 16805, 16806, 16807, 16808, 16809, 16810, 16811, 16812, 16813, 16814, 16815, 16816, 16817, 16818, 16819, 16820, 16821, 16822, 16823, 16824, 16825, 16826, 16827, 16828, 16829, 16830, 16831, 16832, 16833, 16834, 16835, 16836, 16837, 16838, 16839, 16840, 16841, 16842, 16843, 16844, 16845, 16846, 16847, 16848, 16849, 16850, 16851), ProductMarket.NAR),
  WOM_ISR(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5009), ProductMarket.WOM_ISR),
  AUS_NZL(CountryCodeNames.NZL, "TmV3IFplYWxhbmQ=", ImmutableSet.of(23700), ImmutableSet.of(23701), ProductMarket.AUS),
  EUR_FRO(CountryCodeNames.FRO, "SWNlbGFuZCArIEbDuHJveWFyICsgU3ZhbGJhcmQgKE4yK0ZPK1NKKQ==", ImmutableSet.of(11000), ImmutableSet.of(11001), ProductMarket.EUR),
  EUR_DNK(CountryCodeNames.DNK, "RGVubWFyayAoUzQp", ImmutableSet.of(10500), ImmutableSet.of(10501), ProductMarket.EUR),
  SAM_GUF(CountryCodeNames.GUF, "RnJlbmNoIEd1aWFuYQ==", ImmutableSet.of(22800), ImmutableSet.of(22801), ProductMarket.SAM),
  ASIA_SGP(CountryCodeNames.SGP, "TWFsYXlzaWE6U2luZ2Fwb3JlOkJydW5laQ==", ImmutableSet.of(24200), ImmutableSet.of(24202), ProductMarket.ASIA),
  EUR_VAT(CountryCodeNames.VAT, "SXRhbHkgTGF6aW8gKyBWYXRpY2FubyAoSTIp", ImmutableSet.of(11100), ImmutableSet.of(11113), ProductMarket.EUR),
  SA_ZAF(CountryCodeNames.ZAF, "TGltcG9wbytOb3J0aCBXZXN0", ImmutableSet.of(26500), ImmutableSet.of(26501, 26502, 26503, 26504), ProductMarket.SA),
  AGCC_JOR(CountryCodeNames.JOR, "TGliYW5vbiArIEpvcmRhbg==", ImmutableSet.of(25000), ImmutableSet.of(25001), ProductMarket.AGCC),
  SAM_GLP(CountryCodeNames.GLP, "VmVuZXp1ZWxhICsgR3VhZGVsb3VwZSArIE1hcnRpbmlxdWU=", ImmutableSet.of(22600), ImmutableSet.of(22601), ProductMarket.SAM),
  SA_NAM(CountryCodeNames.NAM, "Qm90c3dhbmErTmFtaWJpYQ==", ImmutableSet.of(26300), ImmutableSet.of(26301), ProductMarket.SA),
  WOM_AGCC(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5007), ProductMarket.WOM_AGCC),
  SA_MOZ(CountryCodeNames.MOZ, "TW96YW1iaXF1ZStaaW1iYWJ3ZStSZXVuaW9u", ImmutableSet.of(26400), ImmutableSet.of(26401), ProductMarket.SA),
  SAM_VEN(CountryCodeNames.VEN, "VmVuZXp1ZWxhICsgR3VhZGVsb3VwZSArIE1hcnRpbmlxdWU=", ImmutableSet.of(22600), ImmutableSet.of(22601), ProductMarket.SAM),
  EUR_ISL(CountryCodeNames.ISL, "SWNlbGFuZCArIEbDuHJveWFyICsgU3ZhbGJhcmQgKE4yK0ZPK1NKKQ==", ImmutableSet.of(11000), ImmutableSet.of(11001), ProductMarket.EUR),
  EUR_ESP(CountryCodeNames.ESP, "U3BhaW4gV2VzdGVybiBBbmRhbHVzaWEgKyBHaWJyYWx0YXIgKEsyKQ==", ImmutableSet.of(10600), ImmutableSet.of(10601, 10602, 10603, 10604, 10605, 10606, 10607, 10608, 10609, 10610, 10611, 10612, 10613, 10614, 10615), ProductMarket.EUR),
  SAM_PAN(CountryCodeNames.PAN, "UGFuYW1hICsgQ29zdGEgUmljYQ==", ImmutableSet.of(22900), ImmutableSet.of(22901), ProductMarket.SAM),
  SA_BWA(CountryCodeNames.BWA, "Qm90c3dhbmErTmFtaWJpYQ==", ImmutableSet.of(26300), ImmutableSet.of(26301), ProductMarket.SA),
  EUR_SWE(CountryCodeNames.SWE, "U3dlZGVuIE5vcnRoIChTMyk=", ImmutableSet.of(11500), ImmutableSet.of(11504, 11505, 11501, 11502, 11503), ProductMarket.EUR),
  EUR_EST(CountryCodeNames.EST, "RXN0b25pYSAoSDQp", ImmutableSet.of(12300), ImmutableSet.of(12301), ProductMarket.EUR),
  EUR_MKD(CountryCodeNames.MKD, "TWFjZWRvbmlhICsgQnVsZ2FyaWEgKE00K0g3KQ==", ImmutableSet.of(11800), ImmutableSet.of(11801), ProductMarket.EUR),
  EUR_LIE(CountryCodeNames.LIE, "U3dpdHplcmxhbmQgRWFzdCArIExpZWNodGVuc3RlaW4gKENBKQ==", ImmutableSet.of(10300), ImmutableSet.of(10302), ProductMarket.EUR),
  EUR_BEL(CountryCodeNames.BEL, "QmVsZ2l1bSBOb3J0aCArIEx1eGVtYm91cmcgKEJCKQ==", ImmutableSet.of(10200), ImmutableSet.of(10201, 10202), ProductMarket.EUR),
  SA_REU(CountryCodeNames.REU, "TW96YW1iaXF1ZStaaW1iYWJ3ZStSZXVuaW9u", ImmutableSet.of(26400), ImmutableSet.of(26401), ProductMarket.SA),
  EUR_KOS(CountryCodeNames.UNDEFINED, "S29zb3bDqyAoS1Qp", ImmutableSet.of(12900), ImmutableSet.of(12901), ProductMarket.EUR),
  EUR_RUS(CountryCodeNames.RUS, "Q3JpbWVhIFJlcHVibGljIChSWitQWCk=", ImmutableSet.of(13500), ImmutableSet.of(13504, 13505, 13506, 13507, 13508, 13509, 13510, 13511, 13512, 13513, 13514, 13515, 13516, 13517, 13518, 13519, 13520, 13521, 13522, 13523, 13524, 13525, 13526, 13527, 13528, 13529, 13530, 13531, 13532, 13501, 13502, 13503), ProductMarket.EUR),
  EUR_ALB(CountryCodeNames.ALB, "QWxiYW5pYSArIE1vbnRlbmVncm8gKE0zK01LKQ==", ImmutableSet.of(11600), ImmutableSet.of(11601), ProductMarket.EUR),
  EUR_ITA(CountryCodeNames.ITA, "SXRhbHkgTGF6aW8gKyBWYXRpY2FubyAoSTIp", ImmutableSet.of(11100), ImmutableSet.of(11104, 11105, 11106, 11107, 11108, 11109, 11110, 11111, 11112, 11113, 11101, 11102, 11103), ProductMarket.EUR),
  AGCC_BHR(CountryCodeNames.BHR, "QmFocmFpbiArIFF1YXRhciArIEt1d2FpdA==", ImmutableSet.of(25100), ImmutableSet.of(25101), ProductMarket.AGCC),
  WOM_AUS(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5005), ProductMarket.WOM_AUS),
  NA_DZA(CountryCodeNames.DZA, "QWxnZXJpYQ==", ImmutableSet.of(25900), ImmutableSet.of(25901, 25902), ProductMarket.NA),
  EUR_MCO(CountryCodeNames.MCO, "RnJhbmNlIFByb3ZlbmNlLUFscGVzLUPDtHRlIGQnQXp1ciArIE1vbmFjbyAoRjMp", ImmutableSet.of(10800), ImmutableSet.of(10823), ProductMarket.EUR),
  AGCC_KAZ(CountryCodeNames.KAZ, "S2F6YWtoc3RhbiBTb3V0aHdlc3QgKEtaKQ==", ImmutableSet.of(25200), ImmutableSet.of(25201, 25202, 25203, 25204, 25205, 25206), ProductMarket.AGCC),
  AGCC_SAU(CountryCodeNames.SAU, "U2F1ZGkgQXJhYmlhIFNvdXRo", ImmutableSet.of(24800), ImmutableSet.of(24801, 24802, 24803), ProductMarket.AGCC),
  EUR_UKR(CountryCodeNames.UKR, "QXZ0b25vbW5hIFJlc3B1Ymxpa2EgS3J5bQ==", ImmutableSet.of(14000), ImmutableSet.of(14001, 14002, 14003, 14004, 14005, 14006, 14007, 14008), ProductMarket.EUR),
  SAM_COL(CountryCodeNames.COL, "Tm9ydGggQ29sdW1iaWE=", ImmutableSet.of(22400), ImmutableSet.of(22401, 22402), ProductMarket.SAM),
  WOM_SA(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5011), ProductMarket.WOM_SA),
  EUR_IRL(CountryCodeNames.IRL, "SXJlbGFuZCArIE5vcnRoZXJuIElyZWxhbmQgKEU3K0U0KQ==", ImmutableSet.of(10900), ImmutableSet.of(10908), ProductMarket.EUR),
  SAM_URY(CountryCodeNames.URY, "UGFyYWd1YXkgKyBVcnVndWF5", ImmutableSet.of(22500), ImmutableSet.of(22501), ProductMarket.SAM),
  EUR_NLD(CountryCodeNames.NLD, "TmV0aGVybGFuZHMgV2VzdCAoTkwp", ImmutableSet.of(11200), ImmutableSet.of(11201, 11202), ProductMarket.EUR),
  SA_LSO(CountryCodeNames.LSO, "TGVzb3RobytaQUYoRWFzdGVybiBDYXBlK1dlc3Rlcm4gQ2FwZSk=", ImmutableSet.of(26500), ImmutableSet.of(26502), ProductMarket.SA),
  SA_ZWE(CountryCodeNames.ZWE, "TW96YW1iaXF1ZStaaW1iYWJ3ZStSZXVuaW9u", ImmutableSet.of(26400), ImmutableSet.of(26401), ProductMarket.SA),
  EUR_SVN(CountryCodeNames.SVN, "U2xvdmVuaWEgKEgzKQ==", ImmutableSet.of(13800), ImmutableSet.of(13801), ProductMarket.EUR),
  EUR_SVK(CountryCodeNames.SVK, "U2xvdmFraWEgKE0yKQ==", ImmutableSet.of(13700), ImmutableSet.of(13701), ProductMarket.EUR),
  EUR_BLR(CountryCodeNames.BLR, "QmVsYXJ1cyBFYXN0IChNNyk=", ImmutableSet.of(12000), ImmutableSet.of(12001, 12002), ProductMarket.EUR),
  SAMPLES_DEU(CountryCodeNames.DEU, "U291dGggTG93ZXIgU2F4b255IE5vcnRoIChHNCk=", ImmutableSet.of(1), ImmutableSet.of(32001, 32002, 32003, 32004, 32005, 32006, 32007), ProductMarket.SAMPLES),
  ASIA_MYS(CountryCodeNames.MYS, "TWFsYXlzaWE6U2luZ2Fwb3JlOkJydW5laQ==", ImmutableSet.of(24200), ImmutableSet.of(24201, 24202), ProductMarket.ASIA),
  WOM_SAM(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5003), ProductMarket.WOM_SAM),
  WOM_INDIA(CountryCodeNames.UNDEFINED, "V29ybGQgT3ZlcnZpZXcgTWFw", ImmutableSet.of(1), ImmutableSet.of(5008), ProductMarket.WOM_INDIA);

  private final CountryCodeNames isoCountryCode;
  private final String countryName;
  private final Collection<Integer> baseUrSetIds;
  private final Collection<Integer> urIds;
  private final ProductMarket productMarket;

  private UpdateRegionSet(CountryCodeNames isoCountryCode, String countryName, Collection<Integer> baseUrSetIds, Collection<Integer> urIds,
      ProductMarket productMarket)
  {
    this.isoCountryCode = isoCountryCode;
    this.countryName = countryName;
    this.baseUrSetIds = baseUrSetIds;
    this.urIds = urIds;
    this.productMarket = productMarket;
  }

  public CountryCodeNames getIsoCountryCode()
  {
    return isoCountryCode;
  }

  public String getCountryName()
  {
    return new String(Base64.getDecoder().decode(countryName));
  }

  public Collection<Integer> getBaseUrSetIds()
  {
    return baseUrSetIds;
  }

  public Collection<Integer> getUrIds()
  {
    return urIds;
  }

  public ProductMarket getProductMarket()
  {
    return productMarket;
  }

  public static Collection<UpdateRegionSet> getUpdateRegionSetsByUrSetId(int urSetId)
  {
    Set<UpdateRegionSet> result = new HashSet<>();
    for (UpdateRegionSet updateRegionSet : UpdateRegionSet.values())
    {
      if (updateRegionSet.getBaseUrSetIds().contains(urSetId))
      {
         result.add(updateRegionSet);
      }
    }
    return result;
  }

  public static Collection<UpdateRegionSet> getUpdateRegionSetsByUrId(int urId)
  {
    Set<UpdateRegionSet> result = new HashSet<>();
    for (UpdateRegionSet updateRegionSet : UpdateRegionSet.values())
    {
      if (updateRegionSet.getUrIds().contains(urId))
      {
         result.add(updateRegionSet);
      }
    }
    return result;
  }
}
