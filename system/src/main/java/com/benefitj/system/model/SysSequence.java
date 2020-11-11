package com.benefitj.system.model;//package com.benefit.commons.model;
//
//import lombok.Data;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * 序列用于获取序列值
// * <p>
// * CREATE DEFINER=`root`@`%` FUNCTION `nextId`(sequence_name varchar(64)) RETURNS bigint
// * DETERMINISTIC
// * BEGIN
// * DECLARE current BIGINT;
// * SET current = 0;
// * <p>
// * INSERT INTO sys_sequence(`sequence_name`,`value`) VALUES(sequence_name,1) ON DUPLICATE KEY UPDATE `value` = `value` + 1;
// * SELECT t.value INTO current FROM sys_sequence t WHERE t.sequence_name = sequence_name;
// * <p>
// * RETURN current;
// * END
// */
//@Data
//@Entity
//@Table(name = "sys_sequence")
//public class SysSequence {
//  /**
//   * 序列名
//   */
//  @Id
//  @Column(name = "sequence_name", columnDefinition = "varchar(32) comment '序列名'", length = 32)
//  private String sequenceName;
//  /**
//   * 序列值
//   */
//  @Column(name = "value", columnDefinition = "bigint comment '序列值'")
//  private Long value;
//
//
//  /*
//
//CREATE DEFINER=`root`@`%` FUNCTION `nextId`(sequence_name varchar(64)) RETURNS bigint
//    DETERMINISTIC
//BEGIN
//    DECLARE current BIGINT;
//    SET current = 0;
//
//    INSERT INTO sys_sequence(`sequence_name`,`value`) VALUES(sequence_name,1) ON DUPLICATE KEY UPDATE `value` = `value` + 1;
//    SELECT t.value INTO current FROM sys_sequence t WHERE t.sequence_name = sequence_name;
//
//    RETURN current;
//END
//
//   */
//}
