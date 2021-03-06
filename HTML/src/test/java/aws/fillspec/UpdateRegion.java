package aws.fillspec;

import java.util.Base64;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Generated values of update regions for product market
 *
 * @since Akela_UR_Splitting_V11_18.xlsx
 */
public enum UpdateRegion
{
  UNDEFINED("UNDEFINED", -1, EnumSet.of(UpdateRegionSet.UNDEFINED)),
  US_MISSI_16828("TWlzc2lzc2lwcGk=", 16828, EnumSet.of(UpdateRegionSet.NAR_USA)),
  BR_BA_22304("QmFoaWE=", 22304, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  MYS_1_24201("TWFsYXlzaWE=", 24201, EnumSet.of(UpdateRegionSet.ASIA_MYS)),
  SAU_2_24802("U2F1ZGkgQXJhYmlhIFdlc3Q=", 24802, EnumSet.of(UpdateRegionSet.AGCC_SAU)),
  FRA_F5_PY_10812("RnJhbmNlIFB5csOpbsOpZXMtQXRsYW50aXF1ZXMgKyBMYW5kZXMgKyBMb3QtZXQtR2Fyb25uZSAoRjUp", 10812, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  GBR_E3_CE_10905("RW5nbGFuZCBDZW50cmFsIE5vcnRoIChFMyk=", 10905, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  PRY_URY_22501("UGFyYWd1YXkgKyBVcnVndWF5", 22501, EnumSet.of(UpdateRegionSet.SAM_PRY, UpdateRegionSet.SAM_URY)),
  RUS_R4_13518("UnVzc2lhIE5vcnRoIENhdWNhc3VzIChSNCk=", 13518, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  LTU_13001("TGl0aHVhbmlhIChINik=", 13001, EnumSet.of(UpdateRegionSet.EUR_LTU)),
  AU_QUE_23603("UXVlZW5zbGFuZA==", 23603, EnumSet.of(UpdateRegionSet.AUS_AUS)),
  ESP_K3_NO_10608("U3BhaW4gTm9ydGh3ZXN0IChLMyk=", 10608, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  IDN_4_23904("SW5kb25lc2lh", 23904, EnumSet.of(UpdateRegionSet.ASIA_IDN)),
  MRM_TUR_T4_SOEA_27204("VHVya2V5IFNvdXRoIEVhc3QgKFQ0KQ==", 27204, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  GRC_SOWE_12402("R3JlZWNlIFNvdXRod2VzdCAoR0op", 12402, EnumSet.of(UpdateRegionSet.EUR_GRC)),
  IN_GJ1_24517("R3VqYXJhdDE=", 24517, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  BLR_EAST_12002("QmVsYXJ1cyBFYXN0IChNNyk=", 12002, EnumSet.of(UpdateRegionSet.EUR_BLR)),
  SAM_MEX_4_23004("TWV4aWNvIDQ=", 23004, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  KOS_12901("S29zb3bDqyAoS1Qp", 12901, EnumSet.of(UpdateRegionSet.EUR_KOS)),
  RUS_R6_13521("UnVzc2lhIFVyYWwgKFI2KQ==", 13521, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  DEU_G2_RP_SA_10404("R2VybWFueSBSaGVpbmxhbmQtUGZhbHosIFNhYXJsYW5kIChHMik=", 10404, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  POL_NORTH_13401("UG9sYW5kIE5vcnRoIChQMSk=", 13401, EnumSet.of(UpdateRegionSet.EUR_POL)),
  SWE_EAST_11501("U3dlZGVuIEVhc3QgKFMxKQ==", 11501, EnumSet.of(UpdateRegionSet.EUR_SWE)),
  FRA_F5_CE_10815("RnJhbmNlIFNvdXRoIENlbnRyYWwgKEY1KQ==", 10815, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  SAU_3_24803("U2F1ZGkgQXJhYmlhIFNvdXRo", 24803, EnumSet.of(UpdateRegionSet.AGCC_SAU)),
  KAZ_SOWE_25206("S2F6YWtoc3RhbiBTb3V0aHdlc3QgKEtaKQ==", 25206, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  HUN_12601("SHVuZ2FyeSAoSDEp", 12601, EnumSet.of(UpdateRegionSet.EUR_HUN)),
  FRA_F4_WE_10811("RnJhbmNlIFdlc3Qgdy9vIEJyaXR0YW55IChGNCk=", 10811, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  FRA_F3_CE_10807("RnJhbmNlIENlbnRyYWwgRWFzdCAoRjMp", 10807, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  IN_TN0_24512("VGFtaWwgTmFkdTA=", 24512, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  ALB_MNE_11601("QWxiYW5pYSArIE1vbnRlbmVncm8gKE0zK01LKQ==", 11601, EnumSet.of(UpdateRegionSet.EUR_MNE, UpdateRegionSet.EUR_ALB)),
  US_KY_WV_16823("S2VudHVja3krV2VzdCBWaXJnaW5pYQ==", 16823, EnumSet.of(UpdateRegionSet.NAR_USA)),
  SWE_NO_11505("U3dlZGVuIE5vcnRoIChTMyk=", 11505, EnumSet.of(UpdateRegionSet.EUR_SWE)),
  RUS_RF_13527("UnVzc2lhIE1vc2NvdyBSZWdpb24gKFJGKQ==", 13527, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  TUR_T5_SOEA_13906("VHVya2V5IFNvdXRoZWFzdCAoVDUp", 13906, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  IN_HR_24523("SGFyeWFuYTo=", 24523, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  IN_JK_DA_24529("SmFtbXUgJiBLYXNobWly", 24529, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  EGY_3_25803("RWd5cHQ=", 25803, EnumSet.of(UpdateRegionSet.NA_EGY)),
  VNM_2_24002("VmlldG5hbQ==", 24002, EnumSet.of(UpdateRegionSet.ASIA_VNM)),
  ITA_I1_WE_11101("SXRhbHkgTG9tYmFyZHkgV2VzdCAoSTEp", 11101, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  ITA_I2_11105("SXRhbHkgVHVzY2FueSArIFVtYnJpYSAoSTIp", 11105, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  NAR_MEX_6_16706("TWV4aWNvIDY=", 16706, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  GBR_E2_CEEA_10902("RW5nbGFuZCBDZW50cmFsIEVhc3QgKEUyKQ==", 10902, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  AU_SOUTH_23605("U291dGggQXVzdHJhbGlh", 23605, EnumSet.of(UpdateRegionSet.AUS_AUS)),
  ESP_K9_VAL_10614("U3BhaW4gVmFsZW5jaWEgKEs5KQ==", 10614, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  US_PEN_EAST_16842("UGVubnN5bHZhbmlhMg==", 16842, EnumSet.of(UpdateRegionSet.NAR_USA)),
  IN_UT_HP_24528("VXR0YXJha2hhbmQrSGltYWNoYWwgUHJhZGVzaA==", 24528, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  LBN_JOR_25001("TGliYW5vbiArIEpvcmRhbg==", 25001, EnumSet.of(UpdateRegionSet.AGCC_LBN, UpdateRegionSet.AGCC_JOR)),
  ESP_K2_ANEA_10606("U3BhaW4gRWFzdGVybiBBbmRhbHVzaWEgKEsyKQ==", 10606, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  SWE_GOT_11503("U3dlZGVuIFbDpHN0cmEgR8O2dGFsYW5kcyBDb3VudHkgKFMyKQ==", 11503, EnumSet.of(UpdateRegionSet.EUR_SWE)),
  US_TEN_16848("VGVubmVzc2Vl", 16848, EnumSet.of(UpdateRegionSet.NAR_USA)),
  US_VIR_16849("VmlyZ2luaWE=", 16849, EnumSet.of(UpdateRegionSet.NAR_USA)),
  ITA_I3_CE_11106("SXRhbHkgUHVnbGlhICsgQmFzaWxpY2F0YSAoSTMp", 11106, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  MOZ_ZWE_REU_26401("TW96YW1iaXF1ZStaaW1iYWJ3ZStSZXVuaW9u", 26401, EnumSet.of(UpdateRegionSet.SA_MOZ, UpdateRegionSet.SA_ZWE, UpdateRegionSet.SA_REU)),
  IN_UP1_24502("VXR0YXIgUHJhZGVzaDE=", 24502, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  DZA_2_25902("QWxnZXJpYQ==", 25902, EnumSet.of(UpdateRegionSet.NA_DZA)),
  IN_KA1_KL_24511("S2FybmF0YWthMSArIEtlcmFsYStMYWtzaGFkd2VlcA==", 24511, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  UKR_CE_14003("VWtyYWluZSBDZW50cmFsIChVMik=", 14003, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  DEU_G1_SO_10401("R2VybWFueSBCYXllcm4gU291dGggKEcxKQ==", 10401, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  GBR_E2_SOWE_10903("RW5nbGFuZCBTb3V0aHdlc3QgKEUyKSArIENoYW5uZWwgSXNsYW5kcw==", 10903, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  ITA_I1_SO_11103("SXRhbHkgU291dGggUGllZG1vbnQgKyBMaWd1cmlhIChJMSk=", 11103, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  BS_KY_PR_VI_16901("QmFoYW1hcytDYXltYW4gSXNsYW5kcytQdWVydG8gUmljbytVUyBWaXJnaW4gSXNsYW5kcw==", 16901, EnumSet.of(UpdateRegionSet.NAR_BHS, UpdateRegionSet.NAR_VIR, UpdateRegionSet.NAR_CYM, UpdateRegionSet.NAR_PRI)),
  NAR_MEX_3_16703("TWV4aWNvIDM=", 16703, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  DEU_G3_10405("R2VybWFueSBXZXN0IE5vcnRoIChHMyk=", 10405, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  FIN_EAST_10702("RmlubGFuZCBFYXN0IChTUCk=", 10702, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  FIN_OSTR_10705("RmlubGFuZCBPc3Ryb2JvdGhuaWEgKFNVKQ==", 10705, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  US_MIN_16827("TWlubmVzb3Rh", 16827, EnumSet.of(UpdateRegionSet.NAR_USA)),
  DEU_G6_SO_10410("R2VybWFueSBUw7xiaW5nZW4gKyBGcmVpYnVyZyAoQmFkZW4tV8O8cnR0ZW1iZXJnKSAoRzYp", 10410, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  US_ALAB_16801("QWxhYmFtYQ==", 16801, EnumSet.of(UpdateRegionSet.NAR_USA)),
  PHL_2_24102("UGhpbGlwaW5lcw==", 24102, EnumSet.of(UpdateRegionSet.ASIA_PHL)),
  CYPN_DA_12103("Tm9ydGhlcm4gQ3lwcnVzIERB", 12103, EnumSet.of(UpdateRegionSet.EUR_CYP)),
  BIH_11901("Qm9zbmlhIGFuZCBIZXJ6ZWdvdmluYSAoTTYp", 11901, EnumSet.of(UpdateRegionSet.EUR_BIH)),
  RUS_4A_3_13503("UnVzc2lhIE11cm1hbnNrICsgS2FyZWxpYSAoNEEp", 13503, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  BWA_NAM_26301("Qm90c3dhbmErTmFtaWJpYQ==", 26301, EnumSet.of(UpdateRegionSet.SA_NAM, UpdateRegionSet.SA_BWA)),
  AU_VIC_23602("VmljdG9yaWE=", 23602, EnumSet.of(UpdateRegionSet.AUS_AUS)),
  ITA_I3_SO_11107("SXRhbHkgQ2FtcGFuaWEgKyBDYWxhYnJpYSAoSTMp", 11107, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  US_ID_MT_16817("SWRhaG8rTW9udGFuYQ==", 16817, EnumSet.of(UpdateRegionSet.NAR_USA)),
  GAR_32002("R2FyY2hpbmc=", 32002, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  RUS_R8_R9_13526("UnVzc2lhIFNpYmVyaWEgTm9ydGhlYXN0IChSOCArIFI5KQ==", 13526, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  AUT_WEST_10102("QXVzdHJpYSBXZXN0IChBMSk=", 10102, EnumSet.of(UpdateRegionSet.EUR_AUT)),
  NED_EAST_11201("TmV0aGVybGFuZHMgRWFzdCAoTjcp", 11201, EnumSet.of(UpdateRegionSet.EUR_NLD)),
  RUS_4E_13508("UnVzc2lhIEJlbGdvcm9kICsgVm9yb25lemggKDRFKQ==", 13508, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IN_RJ0_24508("UmFqYXN0aGFuMA==", 24508, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  TUR_T3_CEN_13903("VHVya2V5IENlbnRyYWwgKFQzKQ==", 13903, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  DEU_G4_SA_SO_10406("R2VybWFueSBMb3dlciBTYXhvbnkgU291dGggKEc0KQ==", 10406, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  BRA_JB1_22305("QnJhemlsIEpCMQ==", 22305, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  TH_4_23804("VGhhaWxhbmQ=", 23804, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  MRM_TUR_T3_CEN_27203("VHVya2V5IENlbnRyYWwgKFQzKQ==", 27203, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  AUT_CEN_10103("QXVzdHJpYSBDZW50ZXJhbCAoQTEp", 10103, EnumSet.of(UpdateRegionSet.EUR_AUT)),
  SAM_MEX_3_23003("TWV4aWNvIDM=", 23003, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  DEU_G4_SN_BR_10407("R2VybWFueSBMb3dlciBTYXhvbnkgTm9ydGggKyBCcmVtZW4gKEc0KQ==", 10407, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  FRA_F1_10801("RnJhbmNlIElsZSBkZSBGcmFuY2UgKEYxKQ==", 10801, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  RUS_R3_13517("UnVzc2lhIE1vc2NvdyAoUjMp", 13517, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IN_GJ0_24516("R3VqYXJhdDA=", 24516, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  IN_PB_CH_24525("UHVuamFiK0NoYW5kaWdhcmg=", 24525, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  KAZ_SOEA_25201("S2F6YWtoc3RhbiBTb3V0aGVhc3QgKEtWKQ==", 25201, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  IN_CT_24526("Q2hoYXR0aXNnYXJo", 24526, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  US_ARK_16804("QXJrYW5zYXM=", 16804, EnumSet.of(UpdateRegionSet.NAR_USA)),
  ZAF_EC_WC_LSO_26502("TGVzb3RobytaQUYoRWFzdGVybiBDYXBlK1dlc3Rlcm4gQ2FwZSk=", 26502, EnumSet.of(UpdateRegionSet.SA_LSO, UpdateRegionSet.SA_ZAF)),
  ITA_SIZ_11111("SXRhbHkgU2ljaWxpYSAgKyBNYWx0YQ==", 11111, EnumSet.of(UpdateRegionSet.EUR_MLT, UpdateRegionSet.EUR_ITA)),
  ESP_K2_MAD_10604("U3BhaW4gTWFkcmlkIFJlZ2lvbiAoSzIp", 10604, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  NOR_NOR_11301("Tm9yd2F5IE5vcnRoZXJuICtUcsO4bmRlbGFnIChOMSk=", 11301, EnumSet.of(UpdateRegionSet.EUR_NOR)),
  DEU_G2_HE_10403("R2VybWFueSBIZXNzZSAoRzIp", 10403, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  FIN_SOEA_10703("RmlubGFuZCBTb3V0aGVhc3QgKFNRKQ==", 10703, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  RUS_4A_2_13502("UnVzc2lhIEtvbWkgKyBOZW5ldHMgKDRBKQ==", 13502, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IDN_3_23903("SW5kb25lc2lh", 23903, EnumSet.of(UpdateRegionSet.ASIA_IDN)),
  US_MICH_16826("TWljaGlnYW4=", 16826, EnumSet.of(UpdateRegionSet.NAR_USA)),
  US_IL_SOUTH_16819("SWxsaW5vaXMy", 16819, EnumSet.of(UpdateRegionSet.NAR_USA)),
  US_CA_SOUTH_16808("Q2FsaWZvcm5pYTQ=", 16808, EnumSet.of(UpdateRegionSet.NAR_USA)),
  UKR_SOEA_14007("VWtyYWluZSBTb3V0aGVhc3QgKFU2KQ==", 14007, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  ARE_OMN_24901("RW1pcmF0ZXMgKyBPbWFu", 24901, EnumSet.of(UpdateRegionSet.AGCC_OMN, UpdateRegionSet.AGCC_ARE)),
  RUS_4F_4G_13512("UnVzc2lhIFNtb2xlbnNrICsgQnJ5YW5zayArIEt1cnNrICg0Rys0Rik=", 13512, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  RUS_R8_1_13524("UnVzc2lhIFByaW1vcnNreSBLcmFpIChSOCk=", 13524, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  RUS_R1_R2_13516("UnVzc2lhIFdlc3QgKFIxK1IyKQ==", 13516, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  US_KAN_16822("S2Fuc2Fz", 16822, EnumSet.of(UpdateRegionSet.NAR_USA)),
  IN_WB0_SK_24518("V2VzdCBCZW5nYWwwK1Npa2tpbQ==", 24518, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  PRT_NO_11402("UG9ydHVnYWwgTm9ydGggKEs4KQ==", 11402, EnumSet.of(UpdateRegionSet.EUR_PRT)),
  TUR_T5_NOEA_13907("VHVya2V5IE5vcnRoZWFzdCAoVDUp", 13907, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  DEU_G8_10413("R2VybWFueSBTb3V0aGVhc3QgKEc4KQ==", 10413, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  MAR_26001("TW9yb2Njbw==", 26001, EnumSet.of(UpdateRegionSet.NA_MAR)),
  FRA_F3_MON_10823("RnJhbmNlIFByb3ZlbmNlLUFscGVzLUPDtHRlIGQnQXp1ciArIE1vbmFjbyAoRjMp", 10823, EnumSet.of(UpdateRegionSet.EUR_FRA, UpdateRegionSet.EUR_MCO)),
  DEU_G5_10409("R2VybWFueSBCZXJsaW4gKyBCcmFuZGVuYnVyZyArIFNhY2hzZW4tQW5oYWx0IChHNSk=", 10409, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  ESP_K7_CEN_10612("U3BhaW4gQ2VudHJhbCBOb3J0aCAoSzcp", 10612, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  BRA_JA1_22307("QnJhemlsIEpBMQ==", 22307, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  SAM_MEX_5_23005("TWV4aWNvIDU=", 23005, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  VNM_1_24001("VmlldG5hbQ==", 24001, EnumSet.of(UpdateRegionSet.ASIA_VNM)),
  FRA_F6_SO_10818("RnJhbmNlIENlbnRyYWwgU291dGggKEY2KQ==", 10818, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  RUS_RK_13531("UnVzc2lhIFZvbGdhIENlbnRyYWwgRWFzdCAoUksp", 13531, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  FRA_F5_DO_10813("RnJhbmNlIEdpcm9uZGUgKyBEb3Jkb2duZSAoRjUp", 10813, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  IN_MH1_24506("TWFoYXJhc2h0cmEx", 24506, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  IN_TG_24522("VGVsYW5nYW5h", 24522, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  FIN_NOWE_10707("RmlubGFuZCBOb3J0aHdlc3QgKFNVKQ==", 10707, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  BRE_32005("QnJlbWVt", 32005, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  TUR_T4_SOEA_13904("VHVya2V5IFNvdXRoIEVhc3QgKFQ0KQ==", 13904, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  US_IL_NORTH_16818("SWxsaW5vaXMx", 16818, EnumSet.of(UpdateRegionSet.NAR_USA)),
  CZE_WEST_12201("Q3plY2ggUmVwdWJsaWMgV2VzdCAoQzEp", 12201, EnumSet.of(UpdateRegionSet.EUR_CZE)),
  US_NC_EAST_16836("Tm9ydGggQ2Fyb2xpbmEy", 16836, EnumSet.of(UpdateRegionSet.NAR_USA)),
  FRA_F2_NO_10803("RnJhbmNlIEhhdXRzLWRlLUZyYW5jZSBOb3J0aCAoRjIp", 10803, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  SVN_13801("U2xvdmVuaWEgKEgzKQ==", 13801, EnumSet.of(UpdateRegionSet.EUR_SVN)),
  NED_WEST_11202("TmV0aGVybGFuZHMgV2VzdCAoTkwp", 11202, EnumSet.of(UpdateRegionSet.EUR_NLD)),
  RUS_RH_2_13530("UnVzc2lhIEFkeWdlYSArIEtyYXNub2RhciBLcmFpIChSSCk=", 13530, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  MKD_BGR_11801("TWFjZWRvbmlhICsgQnVsZ2FyaWEgKE00K0g3KQ==", 11801, EnumSet.of(UpdateRegionSet.EUR_MKD, UpdateRegionSet.EUR_BGR)),
  MRM_TUR_T4_SOWE_27205("VHVya2V5IFNvdXRoIFdlc3QgKFQ0KQ==", 27205, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  US_SCAR_16843("U291dGggQ2Fyb2xpbmE=", 16843, EnumSet.of(UpdateRegionSet.NAR_USA)),
  ITA_I3_NO_11108("SXRhbHkgRW1pbGlhLVJvbWFnbmEgKEkzKQ==", 11108, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  CA_NORTH_16603("TnVuYXZ1dCtNYW5pdG9iYQ==", 16603, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  IN_UP2_24503("VXR0YXIgUHJhZGVzaDI=", 24503, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  KAZ_EAST_25202("S2F6YWtoc3RhbiBLYXJhZ2FuZHkgKyBFYXN0IChLViArIEtXKQ==", 25202, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  DEN_10501("RGVubWFyayAoUzQp", 10501, EnumSet.of(UpdateRegionSet.EUR_DNK)),
  IN_UP0_DL_24501("VXR0YXIgUHJhZGVzaDArRGVsaGk=", 24501, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  DEU_G4_SA_2_32007("U291dGggTG93ZXIgU2F4b255IE5vcnRoIChHNCk=", 32007, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  US_ND_SD_WY_16837("Tm9ydGggRGFrb3RhK1NvdXRoIERha290YStXeW9taW5n", 16837, EnumSet.of(UpdateRegionSet.NAR_USA)),
  SWE_SOEA_11502("U3dlZGVuIFNvdXRoZWFzdCAoUzEp", 11502, EnumSet.of(UpdateRegionSet.EUR_SWE)),
  NOR_EAST_11302("Tm9yd2F5IEVhc3QgKE4xKQ==", 11302, EnumSet.of(UpdateRegionSet.EUR_NOR)),
  KAZ_NORTH_25203("S2F6YWtoc3RhbiBOb3J0aCAoS1cp", 25203, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  PRT_SO_11401("UG9ydHVnYWwgU291dGggYW5kIFBvcnR1Z3Vlc2UgQXRsYW50aWMgSXNsYW5kcyAoSzQrSzYp", 11401, EnumSet.of(UpdateRegionSet.EUR_PRT)),
  NAR_MEX_1_16701("TWV4aWNvIDE=", 16701, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  US_OKLA_16839("T2tsYWhvbWE=", 16839, EnumSet.of(UpdateRegionSet.NAR_USA)),
  RUS_4H_13513("UnVzc2lhIFNvdXRoZWFzdCAoNEgp", 13513, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  GRC_MAC_12404("R3JlZWNlIE1hY2Vkb25pYSAoR0sp", 12404, EnumSet.of(UpdateRegionSet.EUR_GRC)),
  ESP_K2_SO_10605("U3BhaW4gU291dGggKEsyKQ==", 10605, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  SAU_1_24801("U2F1ZGkgQXJhYmlhIEVhc3Q=", 24801, EnumSet.of(UpdateRegionSet.AGCC_SAU)),
  IN_DD_24531("RGFtYW4gJiBEaXU=", 24531, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  ITA_I5_11110("SXRhbHkgU2FyZGVnbmEgKEk1KQ==", 11110, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  US_IND_16820("SW5kaWFuYQ==", 16820, EnumSet.of(UpdateRegionSet.NAR_USA)),
  NOR_CEN_11303("Tm9yd2F5IENlbnRyYWwgKE4xKQ==", 11303, EnumSet.of(UpdateRegionSet.EUR_NOR)),
  CA_ON_NOR_16606("T250YXJpbyAx", 16606, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  RUS_RH_1_13529("UnVzc2lhIFJvc3RvdiAoUkgp", 13529, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  TH_5_23805("VGhhaWxhbmQ=", 23805, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  US_OHI_16838("T2hpbw==", 16838, EnumSet.of(UpdateRegionSet.NAR_USA)),
  TH_1_23801("VGhhaWxhbmQ=", 23801, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  DEU_G4_G5_10408("R2VybWFueSBTY2hsZXN3aWctSG9sc3RlaW4rIEhhbWJ1cmcgKyBNZWNrbGVuYnVyZy1Wb3Jwb21tZXJuIChHNCArRzUp", 10408, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  TUR_T1_NOWE_13901("VHVya2V5IE5vcnRod2VzdCAoVDEp", 13901, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  CA_QU_EAST_16610("UXXDqWJlY18y", 16610, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  NAR_MEX_4_16704("TWV4aWNvIDQ=", 16704, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  US_CAL_NORTH_16805("Q2FsaWZvcm5pYTE=", 16805, EnumSet.of(UpdateRegionSet.NAR_USA)),
  FIN_SA_10701("RmlubGFuZCBTb3V0aGVybiBTYXZvbmlhIChTUCk=", 10701, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  POL_SOUTH_13403("UG9sYW5kIFNvdXRoIChQMyk=", 13403, EnumSet.of(UpdateRegionSet.EUR_POL)),
  ESP_K1_BAR_10603("U3BhaW4gQmFyY2Vsb25hICsgR2lyb25hIChLMSk=", 10603, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  BRA_JA2_22308("QnJhemlsIEpBMg==", 22308, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  RUS_4J_2_13515("UnVzc2lhIEJhc2hrb3J0b3N0YW4gKyBPcmVuYnVyZyAoNEop", 13515, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  FRA_F2_WE_10804("RnJhbmNlIE5vcm1hbmR5IFdlc3QgKEYyKQ==", 10804, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  US_DE_DC_MD_16811("RGVsYXdhcmUrRGlzdHJpY3Qgb2YgQ29sdW1iaWErTWFyeWxhbmQ=", 16811, EnumSet.of(UpdateRegionSet.NAR_USA)),
  US_WIS_16851("V2lzY29uc2lu", 16851, EnumSet.of(UpdateRegionSet.NAR_USA)),
  BR_SP_22301("U8OjbyBQYXVsbw==", 22301, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  RUS_R7_1_13522("UnVzc2lhIE9tc2sgKyBLZW1lcm92byArIFRvbXNrIChSNyk=", 13522, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  SAM_MEX_2_23002("TWV4aWNvIDI=", 23002, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  AUT_EAST_10101("QXVzdHJpYSBFYXN0IChBMSk=", 10101, EnumSet.of(UpdateRegionSet.EUR_AUT)),
  RUS_R5_1_13519("UnVzc2lhIFZvbGdhIENlbnRyYWwgV2VzdCAoUjUp", 13519, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  UKR_CEWE_14005("VWtyYWluZSBDZW50cmFsIFdlc3QgKFU0KQ==", 14005, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  IDN_2_23902("SW5kb25lc2lh", 23902, EnumSet.of(UpdateRegionSet.ASIA_IDN)),
  BEL_LUX_10202("QmVsZ2l1bSBOb3J0aCArIEx1eGVtYm91cmcgKEJCKQ==", 10202, EnumSet.of(UpdateRegionSet.EUR_LUX, UpdateRegionSet.EUR_BEL)),
  CA_QU_NORD_16609("UXXDqWJlY18x", 16609, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  RUS_4A_1_13501("UnVzc2lhIE5vdmdvcm9kICsgUHNrb3YgKDRBKQ==", 13501, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IN_KA0_24510("S2FybmF0YWthMA==", 24510, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  FRA_F4_SO_10810("RnJhbmNlIEJyaXR0YW55IFNvdXRod2VzdCAoRjQp", 10810, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  CA_WEST_16602("QnJpdGlzaCBDb2x1bWJpYStZdWtvbg==", 16602, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  US_ARI_16803("QXJpem9uYQ==", 16803, EnumSet.of(UpdateRegionSet.NAR_USA)),
  VEN_GU_MA_22601("VmVuZXp1ZWxhICsgR3VhZGVsb3VwZSArIE1hcnRpbmlxdWU=", 22601, EnumSet.of(UpdateRegionSet.SAM_MTQ, UpdateRegionSet.SAM_GLP, UpdateRegionSet.SAM_VEN)),
  US_MISSO_16829("TWlzc291cmk=", 16829, EnumSet.of(UpdateRegionSet.NAR_USA)),
  RUS_4D_13509("UnVzc2lhIEl2YW5vdm8gKyBLb3N0cm9tYSArIFZsYWRpbWlyICg0RCk=", 13509, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  US_GEO_SOUTH_16814("R2VvcmdpYTE=", 16814, EnumSet.of(UpdateRegionSet.NAR_USA)),
  PAN_CRI_22901("UGFuYW1hICsgQ29zdGEgUmljYQ==", 22901, EnumSet.of(UpdateRegionSet.SAM_CRI, UpdateRegionSet.SAM_PAN)),
  DEU_G4_SA_1_32006("U291dGggTG93ZXIgU2F4b255IFNvdXRoIChHNCk=", 32006, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  US_FLO_SOUTH_16812("RmxvcmlkYTE=", 16812, EnumSet.of(UpdateRegionSet.NAR_USA)),
  RUS_4B_13505("UnVzc2lhIFZvbGdhIFdlc3QgKDRCKQ==", 13505, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  ESP_K7_NO_10611("U3BhaW4gTm9ydGhlYXN0IChLNyk=", 10611, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  CA_ON_MID_16607("T250YXJpbyAy", 16607, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  IN_JH_24527("SmhhcmtoYW5k", 24527, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  GBR_E2_E6_10904("RW5nbGFuZCBDZW50cmFsIFdlc3QgKyBXYWxlcyAoRTIrRTYp", 10904, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  US_ALAS_16802("QWxhc2th", 16802, EnumSet.of(UpdateRegionSet.NAR_USA)),
  MRM_TUR_T2_SOWE_27202("VHVya2V5IFNvdXRod2VzdCAoVDIp", 27202, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  ESP_K1_BAL_10602("U3BhaW4gTGxlaWRhICsgVGFycmFnb25hICsgSWxsZXMgQmFsZWFycyAoSzEp", 10602, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  NZL_23701("TmV3IFplYWxhbmQ=", 23701, EnumSet.of(UpdateRegionSet.AUS_NZL)),
  IN_RJ1_24509("UmFqYXN0aGFuMQ==", 24509, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  US_NJE_16831("TmV3IEplcnNleQ==", 16831, EnumSet.of(UpdateRegionSet.NAR_USA)),
  DEU_G1_NO_10402("R2VybWFueSBCYXllcm4gTm9ydGggKEcxKQ==", 10402, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  FIN_LAP_10708("RmlubGFuZCBMYXBwaSAoU1Yp", 10708, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  US_TX_MID_NO_16845("VGV4YXMgMg==", 16845, EnumSet.of(UpdateRegionSet.NAR_USA)),
  UKR_CRI_DA_14008("QXZ0b25vbW5hIFJlc3B1Ymxpa2EgS3J5bQ==", 14008, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  EGY_1_25801("RWd5cHQ=", 25801, EnumSet.of(UpdateRegionSet.NA_EGY)),
  US_COL_16809("Q29sb3JhZG8=", 16809, EnumSet.of(UpdateRegionSet.NAR_USA)),
  AR_BA_22201("QnVlbm9zIEFpcmVz", 22201, EnumSet.of(UpdateRegionSet.SAM_ARG)),
  ITA_I1_I4_11104("SXRhbHkgTG9tYmFyZHkgRWFzdCArIFRyZW50aW5vLUFsdG8gQWRpZ2UgKEkxK0k0KQ==", 11104, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  TH_2_23802("VGhhaWxhbmQ=", 23802, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  ITA_I2_CEN_11112("SXRhbHkgQ2VudHJhbCArIFNhbiBNYXJpbm8gKEkyKQ==", 11112, EnumSet.of(UpdateRegionSet.EUR_ITA, UpdateRegionSet.EUR_SMR)),
  IN_MH2_24507("TWFoYXJhc2h0cmEy", 24507, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  SWE_SOWE_11504("U3dlZGVuIFNvdXRod2VzdCAoUzIp", 11504, EnumSet.of(UpdateRegionSet.EUR_SWE)),
  FRA_F3_EA_10808("RnJhbmNlIEVhc3QgQ2VudHJhbCAoRjMp", 10808, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  US_NY_NORTH_16834("TmV3IFlvcmsy", 16834, EnumSet.of(UpdateRegionSet.NAR_USA)),
  BR_PR_22309("UGFyYW7DoQ==", 22309, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  US_GEO_NORTH_16815("R2VvcmdpYTI=", 16815, EnumSet.of(UpdateRegionSet.NAR_USA)),
  IN_AP_24520("QW5kaHJhIFByYWRlc2g=", 24520, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  CA_EAST_16604("TmV3IEJydW5zd2ljaytQcmluY2UgRWR3YXJkIElzbGFuZCtOZXdmb3VuZGxhbmQgYW5kIExhYnJhZG9yK05vdmEgU2NvdGlh", 16604, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  US_FLO_NORTH_16813("RmxvcmlkYTI=", 16813, EnumSet.of(UpdateRegionSet.NAR_USA)),
  FIN_CEN_10706("RmlubGFuZCBDZW50cmFsIChTVSk=", 10706, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  ITA_I1_NO_11102("SXRhbHkgTm9ydGggUGllZG1vbnQgKyBBb3N0YSBWYWxsZXkgKEkxKQ==", 11102, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  IN_TN1_PY_24513("VGFtaWwgTmFkdTErIFB1ZHVjaGVycnk=", 24513, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  FIN_NOR_10709("RmlubGFuZCBOb3JyYSBGaW5sYW5kIChTVik=", 10709, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  SAM_MEX_6_23006("TWV4aWNvIDY=", 23006, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  COL_NORTH_22402("Tm9ydGggQ29sdW1iaWE=", 22402, EnumSet.of(UpdateRegionSet.SAM_COL)),
  CYP_12101("Q3lwcnVzL1VOL0JyaXRpc2ggQmFzZQ==", 12101, EnumSet.of(UpdateRegionSet.EUR_CYP)),
  BER_32004("QmVybGlu", 32004, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  GBR_E3_NO_10907("RW5nbGFuZCBOb3J0aCArIElzbGUgb2YgTWFuIChFMyk=", 10907, EnumSet.of(UpdateRegionSet.EUR_GBR, UpdateRegionSet.EUR_IMN)),
  IN_BR_24521("QmloYXI=", 24521, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  IN_UP3_24504("VXR0YXIgUHJhZGVzaDM=", 24504, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  IN_EAST_24530("RWFzdCBJbmRpYSAoVHJpcHVyYSBBcnVuYWNoYWwgUHJhZGVzaCBNZWdoYWxheWEgTWl6b3JhbSBNYW5pcHVyIEFuZGFtYW4gJiBOaWNvYmFyIElzbGFuZHMgTmFnYWxhbmQgQXNzYW0p", 24530, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  UKR_SO_14006("VWtyYWluZSBTb3V0aCAoVTUrVVIp", 14006, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  GRC_SOES_12401("R3JlZWNlIFNvdXRoZWFzdCAoR0op", 12401, EnumSet.of(UpdateRegionSet.EUR_GRC)),
  US_IO_NE_16821("SW93YStOZWJyYXNrYQ==", 16821, EnumSet.of(UpdateRegionSet.NAR_USA)),
  KAZ_WEST_25205("S2F6YWtoc3RhbiAgV2VzdCAoS1op", 25205, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  FRA_F5_SO_10814("RnJhbmNlIFNvdXRoIChGNSk=", 10814, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  ISR_27101("SXNyYWVs", 27101, EnumSet.of(UpdateRegionSet.ISR_ISR)),
  FRA_F2_EA_10805("RnJhbmNlIE5vcm1hbmR5IEVhc3QgKEYyKQ==", 10805, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  RUS_4C_2_13507("UnVzc2lhIEJ1cnlhdGlhICsgWmFiYXlrYWxza3kgS3JhaSArIElya3V0c2sgICg0Qyk=", 13507, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  ESP_K9_MU_10613("U3BhaW4gTXVyY2lhIChLOSk=", 10613, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  RUS_RG_13528("UnVzc2lhIFlhcm9zbGF2bCArIFR2ZXIgKFJHKQ==", 13528, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  RUS_4J_1_13514("UnVzc2lhIFBlcm0gS3JhaSAoNEop", 13514, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  US_ME_NH_VT_16825("TWFpbmUrTmV3IEhhbXBzaGlyZStWZXJtb250", 16825, EnumSet.of(UpdateRegionSet.NAR_USA)),
  ZAF_GA_MP_SWZ_26501("U3dhemlsYW5kK1pBRihHYXV0ZW5nK01wdW1hbGFuZ2Ep", 26501, EnumSet.of(UpdateRegionSet.SA_ZAF, UpdateRegionSet.SA_SWZ)),
  TH_6_23806("VGhhaWxhbmQ=", 23806, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  US_ORE_16840("T3JlZ29u", 16840, EnumSet.of(UpdateRegionSet.NAR_USA)),
  ITA_I4_11109("SXRhbHkgVmVuZXRvICsgRnJpdWxpLVZlbmV6aWEgR2l1bGlhIChJNCk=", 11109, EnumSet.of(UpdateRegionSet.EUR_ITA)),
  IN_MH0_GDN_24505("TWFoYXJhc2h0cmEwK0dvYStEYWRyYSBhbmQgTmFnYXIgSGF2ZWxp", 24505, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  BHR_QAT_KWT_25101("QmFocmFpbiArIFF1YXRhciArIEt1d2FpdA==", 25101, EnumSet.of(UpdateRegionSet.AGCC_KWT, UpdateRegionSet.AGCC_BHR, UpdateRegionSet.AGCC_QAT)),
  AR_NORTH_22203("Tm9ydGggQXJnZW50aW5h", 22203, EnumSet.of(UpdateRegionSet.SAM_ARG)),
  BR_MG_22302("TWluYXMgR2VyYWlz", 22302, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  GBR_E5_10906("U2NvdGxhbmQgKEU1KQ==", 10906, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  US_WASH_16850("V2FzaGluZ3Rvbg==", 16850, EnumSet.of(UpdateRegionSet.NAR_USA)),
  FRA_F3_COR_10806("RnJhbmNlIENvcnNlIChGMyk=", 10806, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  US_NV_UT_16830("TmV2YWRhK1V0YWg=", 16830, EnumSet.of(UpdateRegionSet.NAR_USA)),
  RUS_R8_2_13525("UnVzc2lhIFNpYmVyaWEgRWFzdCAoUjgp", 13525, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  US_TX_MID_SO_16846("VGV4YXMgMw==", 16846, EnumSet.of(UpdateRegionSet.NAR_USA)),
  PER_22701("UGVydQ==", 22701, EnumSet.of(UpdateRegionSet.SAM_PER)),
  ESP_K1_ARA_10601("U3BhaW4gQXJhZ29uIChLMSk=", 10601, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  CA_QU_WEST_16611("UXXDqWJlY18z", 16611, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  RUS_R7_2_13523("UnVzc2lhIEFsdGFpIFJlcHVibGljICsgQWx0YWkgS3JhaSArIE5vdm9zaWJpcnNrICAoUjcp", 13523, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IDN_5_23905("SW5kb25lc2lh", 23905, EnumSet.of(UpdateRegionSet.ASIA_IDN)),
  GBR_E1_10901("VUsgU291dGhlYXN0IEVuZ2xhbmQgKEUxKQ==", 10901, EnumSet.of(UpdateRegionSet.EUR_GBR)),
  GBR_E4_E7_10908("SXJlbGFuZCArIE5vcnRoZXJuIElyZWxhbmQgKEU3K0U0KQ==", 10908, EnumSet.of(UpdateRegionSet.EUR_IRL, UpdateRegionSet.EUR_GBR)),
  US_TX_SOUTH_16847("VGV4YXMgNA==", 16847, EnumSet.of(UpdateRegionSet.NAR_USA)),
  SRB_13602("U2VyYmlhIENlbnRyYWwgKyBWb2p2b2RpbmEgKE01KQ==", 13602, EnumSet.of(UpdateRegionSet.EUR_SRB)),
  UKR_WE_14001("VWtyYWluZSBXZXN0IChVMSk=", 14001, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  US_LOU_16824("TG91aXNpYW5h", 16824, EnumSet.of(UpdateRegionSet.NAR_USA)),
  TUN_26101("VHVuaXNpYQ==", 26101, EnumSet.of(UpdateRegionSet.NA_TUN)),
  TUR_T4_SOWE_13905("VHVya2V5IFNvdXRoIFdlc3QgKFQ0KQ==", 13905, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  FIN_SOWE_10704("RmlubGFuZCBTb3V0aHdlc3QgKFNRKQ==", 10704, EnumSet.of(UpdateRegionSet.EUR_FIN)),
  NAR_MEX_5_16705("TWV4aWNvIDU=", 16705, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  DEU_G7_10412("R2VybWFueSBXZXN0IFNvdXRoIChHNyk=", 10412, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  ESP_K3_LU_10610("U3BhaW4gR2FsaWNpYSBBIENvcnXDsWEgKyBMdWdvIChLMyk=", 10610, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  KAZ_CEWE_25204("S2F6YWtoc3RhbiBDZW50cmFsIFdlc3QgKEtaKQ==", 25204, EnumSet.of(UpdateRegionSet.AGCC_KAZ)),
  CHE_EAST_LIE_10302("U3dpdHplcmxhbmQgRWFzdCArIExpZWNodGVuc3RlaW4gKENBKQ==", 10302, EnumSet.of(UpdateRegionSet.EUR_CHE, UpdateRegionSet.EUR_LIE)),
  CA_SA_16605("U2Fza2F0Y2hld2Fu", 16605, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  FRA_F6_LO_10816("RnJhbmNlIENlbnRyZS1WYWwgZGUgTG9pcmUgKEY2KQ==", 10816, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  SAM_MEX_1_23001("TWV4aWNvIDE=", 23001, EnumSet.of(UpdateRegionSet.SAM_MEX)),
  FRA_F7_EA_10820("RnJhbmNlIEVhc3QgKEY3KQ==", 10820, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  RUS_CRI_13532("Q3JpbWVhIFJlcHVibGljIChSWitQWCk=", 13532, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  BR_RS_22310("UmlvIEdyYW5kZSBkbyBTdWw=", 22310, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  BRA_JD1_22303("RXNww61yaXRvIFNhbnRvK1JpbyBkZSBKYW5laXJv", 22303, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  GRC_CEN_12403("R3JlZWNlIENlbnRyYWwgTm90aCAoR0sp", 12403, EnumSet.of(UpdateRegionSet.EUR_GRC)),
  LVA_13101("TGF0dmlhIChINSk=", 13101, EnumSet.of(UpdateRegionSet.EUR_LVA)),
  UKR_NOEA_14004("VWtyYWluZSBOb3J0aGVhc3QgKFUzKQ==", 14004, EnumSet.of(UpdateRegionSet.EUR_UKR)),
  ITA_I2_VAT_11113("SXRhbHkgTGF6aW8gKyBWYXRpY2FubyAoSTIp", 11113, EnumSet.of(UpdateRegionSet.EUR_ITA, UpdateRegionSet.EUR_VAT)),
  EGY_2_25802("RWd5cHQ=", 25802, EnumSet.of(UpdateRegionSet.NA_EGY)),
  NAR_MEX_2_16702("TWV4aWNvIDI=", 16702, EnumSet.of(UpdateRegionSet.NAR_MEX)),
  CHE_WEST_10301("U3dpdHplcmxhbmQgV2VzdCAoQ0Ep", 10301, EnumSet.of(UpdateRegionSet.EUR_CHE)),
  DEU_G6_NO_10411("R2VybWFueSBTdHV0dGdhcnQgKyBLYXJsc3J1aGUgKEJhZGVuLVfDvHJ0dGVtYmVyZyk=", 10411, EnumSet.of(UpdateRegionSet.EUR_DEU)),
  ESP_K2_ANWE_10615("U3BhaW4gV2VzdGVybiBBbmRhbHVzaWEgKyBHaWJyYWx0YXIgKEsyKQ==", 10615, EnumSet.of(UpdateRegionSet.EUR_GIB, UpdateRegionSet.EUR_ESP)),
  CZE_EAST_12202("Q3plY2ggUmVwdWJsaWMgRWFzdCAoQzIp", 12202, EnumSet.of(UpdateRegionSet.EUR_CZE)),
  RUS_4C_1_13506("UnVzc2lhIEtyYXNub3lhcnNrICsgS2hha2Fzc2lhICsgVHV2YSAoNEMp", 13506, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  AR_SOUTH_22202("U291dGggQXJnZW50aW5h", 22202, EnumSet.of(UpdateRegionSet.SAM_ARG)),
  ESP_K2_K5_10607("U3BhaW4gSXNsYXMgQ2FuYXJpYXMgKyBBdXRvbm9tb3VzIGNpdGllcyAoSzUrSzIp", 10607, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  BLR_WEST_12001("QmVsYXJ1cyBXZXN0IChNNyk=", 12001, EnumSet.of(UpdateRegionSet.EUR_BLR)),
  BEL_SOU_10201("QmVsZ2l1bSBTb3V0aCAoQkIp", 10201, EnumSet.of(UpdateRegionSet.EUR_BEL)),
  CA_NOEA_16601("QWxiZXJ0YStOb3J0aHdlc3QgVGVycml0b3JpZXM=", 16601, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  TUR_T2_SOWE_13902("VHVya2V5IFNvdXRod2VzdCAoVDIp", 13902, EnumSet.of(UpdateRegionSet.EUR_TUR)),
  TH_3_23803("VGhhaWxhbmQ=", 23803, EnumSet.of(UpdateRegionSet.ASIA_THA)),
  ZAF_LI_NW_26504("TGltcG9wbytOb3J0aCBXZXN0", 26504, EnumSet.of(UpdateRegionSet.SA_ZAF)),
  US_CT_RI_MA_16810("Q29ubmVjdGljdXQrUmhvZGUgSXNsYW5kK01hc3NhY2h1c2V0dHM=", 16810, EnumSet.of(UpdateRegionSet.NAR_USA)),
  BRA_JB2_22306("QnJhemlsIEpCMg==", 22306, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  IN_WB1_24519("V2VzdCBCZW5nYWwx", 24519, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  MRM_TUR_T5_NOEA_27207("VHVya2V5IE5vcnRoZWFzdCAoVDUp", 27207, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  NOR_WEST_11304("Tm9yd2F5IFdlc3Rlcm4gICsgU291dGhlcm4gKE4xKQ==", 11304, EnumSet.of(UpdateRegionSet.EUR_NOR)),
  FRA_F4_NO_10809("RnJhbmNlIEJyaXR0YW55IE5vcnRoZWFzdCAoRjQp", 10809, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  IN_MP0_24514("TWFkaHlhIFByYWRlc2gw", 24514, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  DZA_1_25901("QWxnZXJpYQ==", 25901, EnumSet.of(UpdateRegionSet.NA_DZA)),
  MRM_TUR_T5_SOEA_27206("VHVya2V5IFNvdXRoZWFzdCAoVDUp", 27206, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  FRA_F6_NO_10817("RnJhbmNlIENlbnRyYWwgTm9ydGggKEY2KQ==", 10817, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  COL_SOUTH_22401("U291dGggQ29sdW1iaWE=", 22401, EnumSet.of(UpdateRegionSet.SAM_COL)),
  IN_MP1_24515("TWFkaHlhIFByYWRlc2gx", 24515, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  ESP_K3_PO_10609("U3BhaW4gR2FsaWNpYSBQb250ZXZlZHJhICsgT3VyZW5zZSAoSzMp", 10609, EnumSet.of(UpdateRegionSet.EUR_ESP)),
  ISL_FRO_SJM_11001("SWNlbGFuZCArIEbDuHJveWFyICsgU3ZhbGJhcmQgKE4yK0ZPK1NKKQ==", 11001, EnumSet.of(UpdateRegionSet.EUR_FRO, UpdateRegionSet.EUR_ISL, UpdateRegionSet.EUR_SJM)),
  FRA_F2_SO_10802("RnJhbmNlIEhhdXRzLWRlLUZyYW5jZSBTb3V0aCAoRjIp", 10802, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  CA_ON_SOU_16608("T250YXJpbyAz", 16608, EnumSet.of(UpdateRegionSet.NAR_CAN)),
  AU_NSW_ACT_23601("TmV3IFNvdXRoIFdhbGVzK0F1c3RyYWxpYW4gQ2FwaXRhbCBUZXJyaXRvcnk=", 23601, EnumSet.of(UpdateRegionSet.AUS_AUS)),
  GUF_22801("RnJlbmNoIEd1aWFuYQ==", 22801, EnumSet.of(UpdateRegionSet.SAM_GUF)),
  FRA_F7_NO_10819("RnJhbmNlIE5vcnRoIENlbnRyYWwgIChGNyk=", 10819, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  US_NME_16832("TmV3IE1leGljbw==", 16832, EnumSet.of(UpdateRegionSet.NAR_USA)),
  AU_WEST_23604("V2VzdGVybiBBdXN0cmFsaWE=", 23604, EnumSet.of(UpdateRegionSet.AUS_AUS)),
  ZAF_FS_KN_NC_26503("RnJlZSBTdGF0ZStLd2F6dWx1IE5hdGFsK05vcnRoZXJuIENhcGU=", 26503, EnumSet.of(UpdateRegionSet.SA_ZAF)),
  RUS_R5_2_13520("UnVzc2lhIE5pemhueSBOb3Znb3JvZCAoUjUp", 13520, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  CHL_SA_27302("Q2hpbGUgTm9ydGg=", 27302, EnumSet.of(UpdateRegionSet.CHL_CHL)),
  RUS_4A_4_13504("UnVzc2lhIEFya2hhbmdlbHNrICsgVm9sb2dkYSAoNEEp", 13504, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  IN_OD_24524("T2Rpc2hh", 24524, EnumSet.of(UpdateRegionSet.INDIA_IND)),
  US_PEN_WEST_16841("UGVubnN5bHZhbmlhMQ==", 16841, EnumSet.of(UpdateRegionSet.NAR_USA)),
  EST_12301("RXN0b25pYSAoSDQp", 12301, EnumSet.of(UpdateRegionSet.EUR_EST)),
  MUC_32001("TXVuaWNo", 32001, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  PHL_1_24101("UGhpbGlwaW5lcw==", 24101, EnumSet.of(UpdateRegionSet.ASIA_PHL)),
  IDN_1_23901("SW5kb25lc2lh", 23901, EnumSet.of(UpdateRegionSet.ASIA_IDN)),
  US_NC_WEST_16835("Tm9ydGggQ2Fyb2xpbmEx", 16835, EnumSet.of(UpdateRegionSet.NAR_USA)),
  CHL_NR_27301("Q2hpbGUgU291dGg=", 27301, EnumSet.of(UpdateRegionSet.CHL_CHL)),
  US_NY_SOUTH_16833("TmV3IFlvcmsx", 16833, EnumSet.of(UpdateRegionSet.NAR_USA)),
  MYS_SGP_BRN_24202("TWFsYXlzaWE6U2luZ2Fwb3JlOkJydW5laQ==", 24202, EnumSet.of(UpdateRegionSet.ASIA_BRN, UpdateRegionSet.ASIA_SGP, UpdateRegionSet.ASIA_MYS)),
  RUS_4E_4D_13510("UnVzc2lhIExpcGV0c2sgKyBUYW1ib3YgKyBSeWF6YW4gKDRFKzREKQ==", 13510, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  US_CAL_MID_16806("Q2FsaWZvcm5pYTI=", 16806, EnumSet.of(UpdateRegionSet.NAR_USA)),
  FRA_F7_NE_10821("RnJhbmNlIE5vcnRoZWFzdCAgKEY3KQ==", 10821, EnumSet.of(UpdateRegionSet.EUR_FRA)),
  HRV_12501("Q3JvYXRpYSAoSDIp", 12501, EnumSet.of(UpdateRegionSet.EUR_HRV)),
  US_CAL_LA_16807("Q2FsaWZvcm5pYTM=", 16807, EnumSet.of(UpdateRegionSet.NAR_USA)),
  POL_EAST_13402("UG9sYW5kIEVhc3QgKFAyKQ==", 13402, EnumSet.of(UpdateRegionSet.EUR_POL)),
  RUS_4F_13511("UnVzc2lhIEthbHVnYSArIE9yeW9sICsgVHVsYSAoNEYp", 13511, EnumSet.of(UpdateRegionSet.EUR_RUS)),
  SRB_KOS_DA_13601("S29zb3ZvIChTZXJiaWFuIFZpZXcpIChNNSk=", 13601, EnumSet.of(UpdateRegionSet.EUR_SRB)),
  SVK_13701("U2xvdmFraWEgKE0yKQ==", 13701, EnumSet.of(UpdateRegionSet.EUR_SVK)),
  US_HAW_16816("SGF3YWlp", 16816, EnumSet.of(UpdateRegionSet.NAR_USA)),
  US_TX_NORTH_16844("VGV4YXMgMQ==", 16844, EnumSet.of(UpdateRegionSet.NAR_USA)),
  UKR_NOWE_MDA_14002("VWtyYWluZSBOb3J0aHdlc3QgKyBNb2xkb3ZhIChVMStVNCtIOSk=", 14002, EnumSet.of(UpdateRegionSet.EUR_MDA, UpdateRegionSet.EUR_UKR)),
  FRA_F5_AND_10822("RnJhbmNlIFNvdXRod2VzdCArIEFuZG9ycmEgKEY1KQ==", 10822, EnumSet.of(UpdateRegionSet.EUR_FRA, UpdateRegionSet.EUR_AND)),
  STU_32003("U3R1dHRnYXJ0", 32003, EnumSet.of(UpdateRegionSet.SAMPLES_DEU)),
  MRM_TUR_T1_NOWE_27201("VHVya2V5IE5vcnRod2VzdCAoVDEp", 27201, EnumSet.of(UpdateRegionSet.TUR_TUR)),
  BR_SC_22311("U2FudGEgQ2F0YXJpbmE=", 22311, EnumSet.of(UpdateRegionSet.SAM_BRA)),
  ROU_13301("Um9tYW5pYSAoSDgp", 13301, EnumSet.of(UpdateRegionSet.EUR_ROU));

  private final String stateName;
  private final Integer urId;
  private final Set<UpdateRegionSet> updateRegionSets;

  private UpdateRegion(String stateName, Integer urId, Set<UpdateRegionSet> updateRegionSets)
  {
    this.stateName = stateName;
    this.urId = urId;
    this.updateRegionSets = updateRegionSets;
  }

  public String getStateName()
  {
    return new String(Base64.getDecoder().decode(stateName));
  }

  public Integer getUrId()
  {
    return urId;
  }

  public Set<UpdateRegionSet> getUpdateRegionSets()
  {
    return updateRegionSets;
  }

  public static Collection<UpdateRegion> getUpdateRegionsByUrId(int urId)
  {
    Set<UpdateRegion> result = new HashSet<>();
    for (UpdateRegion updateRegion : UpdateRegion.values())
    {
      if (Objects.equals(updateRegion.getUrId(), urId))
      {
        result.add(updateRegion);
      }
    }
    return result;
  }
}
