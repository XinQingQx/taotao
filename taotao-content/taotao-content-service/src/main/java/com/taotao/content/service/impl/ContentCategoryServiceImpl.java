package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper mapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
        //1.注入mapper
        //2.创建example
        TbContentCategoryExample example = new TbContentCategoryExample();

        //3.设置条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //4.执行查询
        List<TbContentCategory> list = mapper.selectByExample(example);
        
        //5.转成EasyUITreeNode 列表
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());//分类名称
            nodes.add(node);
        }
        //6.返回
        return nodes;
    }

    /**
     * @param parentId 父节点的id
     * @param name     新增节点的名称
     * @return
     */
    @Override
    public TaotaoResult creatContentCategory(Long parentId, String name) {
        //1.构建对象，补全属性
        TbContentCategory category = new TbContentCategory();
        category.setName(name);
        category.setParentId(parentId);
        category.setCreated(new Date());
        category.setIsParent(false);//新增的节点都是叶子节点
        category.setStatus(1);
        category.setSortOrder(1);
        category.setUpdated(category.getCreated());

        mapper.insertSelective(category);
        //3.返回TaotaoResult 包含内容分类id 需主键返回（修改mapper.xml）

        //判断如果要添加的节点的父节点本身是子节点 需更新其为父节点
        TbContentCategory category1 = mapper.selectByPrimaryKey(parentId);
        if (category1.getIsParent()==false){//原本就是子节点
            category1.setIsParent(true);
            mapper.updateByPrimaryKeySelective(category1);//更新节点的IsParent属性为true
        }

        return TaotaoResult.ok(category);
    }

    /**
     * 更新节点名称
     *
     * @param id   选中的节点id
     * @param name 修改的名称
     */
    @Override
    public TaotaoResult updateContentCategoryListName(Long id, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        mapper.updateByPrimaryKeySelective(category);

        return TaotaoResult.ok(category);
    }

    /**
     * 删除节点
     *
     * @param id 删除节点的id
     * @return
     */
    @Override
    public void deleteContentCategoryListName(Long id) {
        TbContentCategory category = mapper.selectByPrimaryKey(id);
        if (category.getIsParent()==false){//原本就是子节点

            Long parentId = category.getParentId();//父节点的id

            //创建example ,添加查询条件
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(parentId);//查询 符合parentId的信息


            int i = mapper.countByExample(example);
            System.out.println(i);
            if (i==1){
                //更新父节点为子节点
                TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
                parent.setIsParent(false);
                mapper.updateByPrimaryKeySelective(parent);
            }


            mapper.deleteByPrimaryKey(id);
        }
//        return null;
    }
}
