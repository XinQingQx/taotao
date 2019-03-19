package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper mapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;
    /**
     * 根据内容分类的ID 查询其下的内容列表
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {

        //有缓存，读取内容
        try {
            String json = jedisClient.hget(CONTENT_KEY, categoryId + "");
            if (StringUtils.isNotBlank(json)){
                System.out.println("有缓存");
                return JsonUtils.jsonToList(json,TbContent.class );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbContentExample example = new TbContentExample();
        //3.设置查询的条件
        example.createCriteria().andCategoryIdEqualTo(categoryId);//select × from tbcontent where category_id=1

        List<TbContent> list = mapper.selectByExample(example);

        //存入缓存
        try {
            System.out.println("没缓存");
            jedisClient.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

    /**
     * 新增内容表一条记录
     *
     * @param content
     * @return
     */
    @Override
    public TaotaoResult saveContent(TbContent content) {

        content.setCreated(new Date());
        content.setUpdated(content.getCreated());

        mapper.insertSelective(content);

        try {
            jedisClient.hdel(CONTENT_KEY,content.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    /**
     * 显示内容管理表内容
     * categoryId=89&page=1&rows=20
     *
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getItemList(Long categoryId, Integer page, Integer rows) {
        if (page==null)page=1;
        if (rows==null)rows=20;
//        if (categoryId==null)categoryId=0;

        //1.设置分页信息
        PageHelper.startPage(page,rows );
        //2.创建example 对象 设置查询条件
        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);

        List<TbContent> list = mapper.selectByExample(example);
        //3.获取分页的信息
        PageInfo<TbContent> info = new PageInfo<>(list);

        //4.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int)info.getTotal());
        result.setRows(info.getList());

        return result;
    }

    /**
     * 删除内容管理表一条记录
     *
     * @param ids
     * @return
     */
    @Override
    public TaotaoResult deleteContent(Long ids) {

        try {
            Long categoryId = mapper.selectByPrimaryKey(ids).getCategoryId();

            jedisClient.hdel(CONTENT_KEY,categoryId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapper.deleteByPrimaryKey(ids);

        return TaotaoResult.ok();
    }




}
