<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
    <%-- 文档加载后处理以下的逻辑 --%>
$(function(){
    //在#contentCategory 所在的标签中创建一颗树
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
        //右击鼠标触发
		onContextMenu: function(e,node){
		    //关闭原来的鼠标事件
            e.preventDefault();
            //选中右击鼠标的节点
            $(this).tree('select',node.target);
            //展示菜单栏
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,//在鼠标位置显示菜单
                top: e.pageY
            });
        },

        //在选中的节点被在编辑之后触发
        onAfterEdit : function(node){
        	//获取树本身
		    var _tree = $(this);

		    //node.id == 0  表示的是新增的节点
        	if(node.id == 0){
        		// 新增节点
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        			    //更新节点
        				_tree.tree("update",{
            				target : node.target,//更新那个节点
            				id : data.data.id//更新新增节点的id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
//处理点击菜单事件
function menuHandler(item){
    //获取树
	var tree = $("#contentCategory");
	//获取被选中的节点 就是右击鼠标时所在的节点
	var node = tree.tree("getSelected");

	//判断选择的是添加、重命名和删除

    // ==   1==1 true 1=="1" true
    // ===   1===1 true 1==="1" false 类型也相同
	if(item.name === "add"){
	    //在被点击的节点下追加一个子节点
		tree.tree('append', {
            parent: (node?node.target:null),//被添加子节点的父
            data: [{
                text: '新建分类',//节点的内容
                id : 0,//节点的ID
                parentId : node.id//新建的节点的父节点ID
            }]
        });
		//找到id为0的节点---添加节点（新建节点）
		var _node = tree.tree('find',0);//根节点

		//选中id为0的节点 添加节点 开启编辑
        tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>