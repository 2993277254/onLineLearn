package ${controller_package}.${moduleName};

import com.github.pagehelper.PageInfo;
import ${controller_package}.${module}.BaseController;
import ${ProjectPage}.exception.InvalidParamException;
import ${ProjectPage}.sys.Result;
import ${model_package}.${DomainObjectName};
import ${model_package}.${DomainObjectName}Example;
import ${model_package}.ext.${DomainObjectName}Edit;
import ${model_package}.ext.${DomainObjectName}List;
import ${service_package}.I${DomainObjectName}Service;
import ${ProjectPage}.util.MessageHelper;
import ${ProjectPage}.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by on 18-8-24 下午6:13
 * ${DomainObjectName}的控制器业务处理页面
 */

@Controller
@RequestMapping(value = "${FileName}")
public class ${DomainObjectName}Controller extends BaseController{
    public static final Logger logger = LoggerFactory.getLogger(${DomainObjectName}Controller.class);

    @Autowired
    private I${DomainObjectName}Service i${DomainObjectName}Service;
    /**
     * 获取全部列表，通常用于下拉框
     * @param request
     * @param ${FileName}
     * @return
     */
    @RequestMapping(value = "/getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getLists(HttpServletRequest request,${DomainObjectName} ${FileName}){
        Result result = new Result();
        ${DomainObjectName}Example example = new ${DomainObjectName}Example();
        ${DomainObjectName}Example.Criteria criteria = example.createCriteria();
        criteria.and${Status}NotEqualTo("${status_delete}");
        List<${DomainObjectName}> list=i${DomainObjectName}Service.selectByExample(example);
        result.setBizData(list);
        return result;
    }

    /**
     * 获取分页列表，用于页面的列表查询
     * @param request
     * @param ${FileName}List
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public PageInfo list(HttpServletRequest request,  ${DomainObjectName}List ${FileName}List){
        Result result = new Result();
        PageInfo pageInfo=i${DomainObjectName}Service.list(${FileName}List);
        return pageInfo;
    }

    /**
     * 获取实体信息
     * @param request
     * @param ${FileName}Edit
     * @return
     */
    @RequestMapping(value = "/getInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public Result getInfo(HttpServletRequest request,  ${DomainObjectName}Edit ${FileName}Edit){
        Result result = new Result();
        if(ToolUtil.isEmpty(${FileName}Edit.get${id_JAVA}())){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        ${DomainObjectName}Edit ${FileName}=i${DomainObjectName}Service.getInfo(${FileName}Edit.get${id_JAVA}());
        result.setBizData(${FileName});
        return result;
    }

    /**
     * 保存实体信息
     * @param request
     * @param ${FileName}Edit
     * @return
     */
    @RequestMapping(value = "/saveOrEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public Result saveOrEdit(HttpServletRequest request,  ${DomainObjectName}Edit ${FileName}Edit){
        Result result = new Result();
        if(${FileName}Edit==null){
            throw new InvalidParamException(MessageHelper.MODEL_ISNULL);  //抛出异常
        }
        int code=i${DomainObjectName}Service.saveOrEdit(${FileName}Edit);
        result.setBizData(code);
        return result;
    }

    /**
     * 删除实体信息
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(HttpServletRequest request,@RequestParam("ids[]") String[] ids){
        Result result = new Result();
        if(ids!=null&&ids.length==0){
            throw new InvalidParamException(MessageHelper.ID_ISNULL);  //抛出异常
        }
        int code=i${DomainObjectName}Service.delete(ids);
        result.setBizData(code);
        return result;
    }
}
