package com.github.app.api.dao.mapper;

import com.github.app.api.dao.domain.Protocol;
import com.github.app.api.dao.domain.ProtocolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProtocolMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    long countByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int deleteByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int deleteByPrimaryKey(Integer protocolId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int insert(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int insertSelective(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    List<Protocol> selectByExampleWithBLOBs(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    List<Protocol> selectByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    Protocol selectByPrimaryKey(Integer protocolId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByExampleSelective(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByExampleWithBLOBs(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByExample(@Param("record") Protocol record, @Param("example") ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByPrimaryKeySelective(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByPrimaryKeyWithBLOBs(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated Fri Jun 15 17:17:50 CST 2018
     */
    int updateByPrimaryKey(Protocol record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    Protocol selectOneByExample(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    Protocol selectOneByExampleWithBLOBs(ProtocolExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsert(@Param("list") List<Protocol> list);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table protocol
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int batchInsertSelective(@Param("list") List<Protocol> list, @Param("selective") Protocol.Column ... selective);
}