package id.co.asyst.commons.sample.controller;

import javax.validation.Valid;

import id.co.asyst.commons.core.client.RESTClient;
import id.co.asyst.commons.core.service.*;
import id.co.asyst.commons.log.model.Log;
import id.co.asyst.commons.log.service.LogParameter;
import id.co.asyst.commons.log.service.LogRequest;
import id.co.asyst.commons.sample.model.Sample;
import id.co.asyst.commons.sample.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.asyst.commons.core.controller.BaseController;
import id.co.asyst.commons.sample.repository.SampleRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@CrossOrigin(origins="*")
@RestController
@RequestMapping("/sample")
public class SampleController extends BaseController {

    private static final long serialVersionUID = -2364914933763712059L;

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private SampleRepository sampleRepository;

    @Value("${asyst.commons.log.endpoint}")
    private String commonsLogUrlEndpoint;

    @PostMapping("/create")
    public SampleResponse create(@Valid @RequestBody SampleRequest request) {
        /*
        ========================
        Validate Request (if any)
        ========================
        */
        Identity identity = request.getIdentity();
        Paging paging = request.getPaging();
        Parameter parameter = request.getParameter();
        Sample sample = ((SampleParameter) parameter).getSample();
        sample.init();
        String serviceID = UUID.randomUUID().toString();

        /*
        ========================
        Log Input Request
        ========================
        */
        try {
            Log log = new Log();
            log.setStatus(Log.START);
            log.setLevel(Log.INFO);
            log.setSvctxnid(serviceID);
            log.setAppid(identity.getAppid());
            log.setReqtxnid(identity.getReqtxnid());
            log.setData(new ObjectMapper().writeValueAsString(request));

            RESTClient.post(commonsLogUrlEndpoint, new LogRequest(identity, new LogParameter(log)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        ========================
        Process
        ========================
        */

        // Build Response
        SampleResponse response = new SampleResponse();
        response.setIdentity(identity);
        response.setPaging(paging);

        try {
            sample = sampleRepository.save(sample);

            response.setResult(sample);
            response.setStatus(new Status(Status.SUCCESS_CODE, Status.SUCCESS_DESC));
        } catch (Exception e) {
            response.setStatus(new Status(Status.ERROR_CODE, Status.ERROR_CODE, e.getMessage(), e.toString()));
        }

        /*
        ========================
        Log Output Response
        ========================
        */
        try {
            Log log = new Log();
            log.setStatus(Log.FINISH);
            log.setLevel(Log.INFO);
            log.setSvctxnid(serviceID);
            log.setAppid(identity.getAppid());
            log.setReqtxnid(identity.getReqtxnid());
            log.setData(new ObjectMapper().writeValueAsString(response));

            RESTClient.post(commonsLogUrlEndpoint, new LogRequest(identity, new LogParameter(log)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }

    @PostMapping("/retrieve")
    public SampleListResponse retrieve(@Valid @RequestBody SampleRequest request) {
        /*
        ========================
        Validate Request (if any)
        ========================
        */
        Identity identity = request.getIdentity();
        Paging paging = request.getPaging();
        SampleParameter parameter = request.getParameter();

        // Build Response
        SampleListResponse response = new SampleListResponse();
        response.setIdentity(identity);
        /*
        ========================
        Process
        ========================
        */
        try {
            List<Sample> sampleList = null;
            if (paging != null && parameter == null) {
                // Retrieve Without Parameter and With Paging
                System.out.println("retrieveAllWithPaging");
                Page<Sample> sampleListWithPage = retrieveAllWithPaging(paging);
                sampleList = sampleListWithPage.getContent();

                // Set Response Paging
                paging.setTotalpage(sampleListWithPage.getTotalPages());
                paging.setTotalrecord(sampleListWithPage.getTotalElements());

            } else if (paging != null && parameter != null) {
                // Retrieve With Parameter and Paging
                System.out.println("retrieveAllWithParameterPaging");
                Page<Sample> sampleListWithPage = retrieveAllWithParameterPaging(parameter, paging);
                sampleList = sampleListWithPage.getContent();

                // Set Response Paging
                paging.setTotalpage(sampleListWithPage.getTotalPages());
                paging.setTotalrecord(sampleListWithPage.getTotalElements());
            } else if (paging == null && parameter == null) {
                 // Retrieve Without Parameter and Paging
                System.out.println("retrieveAll");
                sampleList = retrieveAll();
            } else if (paging == null && parameter != null) {
                // Retrieve With Parameter and Without Paging
                // execute Custom Query
                System.out.println("retrieveAllWithParameter");
                sampleList = retrieveAllWithParameter(parameter);
            }
            // Set Response Result
            response.setResult(sampleList);
            response.setPaging(paging);

            // Set Response Status
            response.setStatus(new Status(Status.SUCCESS_CODE, Status.SUCCESS_DESC));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(new Status(Status.ERROR_CODE, Status.ERROR_CODE, e.getMessage(), e.toString()));
        }
        return response;
    }

    private List<Sample> retrieveAll() {
        List<Sample> sampleList = null;
        try {
            sampleList = sampleRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    private Page<Sample> retrieveAllWithPaging(Paging paging) {
        Page<Sample> sampleList = null;
        try {
            sampleList = sampleRepository.findAll(PageRequest.of(paging.getPage() - 1, paging.getLimit()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    private Page<Sample> retrieveAllWithParameterPaging(SampleParameter parameter, Paging paging) {
        // parameterSort
        Map<String,String> parameterSort = parameter.getSort();
        Sort sort = null;
        if (parameterSort != null && parameterSort.size() > 0) {
            System.out.println("parameterSort = " + parameterSort);
            for (String key : parameterSort.keySet()) {
                String value = parameterSort.get(key).toUpperCase();
                Sort.Direction direction = Sort.Direction.valueOf(value);
                if (sort == null)
                    sort = Sort.by(direction, key);
                else
                    sort = sort.and(Sort.by(direction, key));
            }
        } else {
            sort = Sort.unsorted();
        }

        // parameterCriteria
        Map<String,String> parameterCriteria = parameter.getCriteria();
        if (parameterCriteria != null && parameterCriteria.size() > 0) {
            System.out.println("parameterCriteria = " + parameterCriteria);
            for (String key : parameterCriteria.keySet()) {
                String value = parameterCriteria.get(key);
            }
        }

        // parameterColumn
        List<String> parameterColumn = parameter.getColumn();
        if (parameterColumn != null && parameterColumn.size() > 0) {
            System.out.println("parameterColumn = " + parameterColumn);

        }

        // Pageable
        Pageable pageable = null;
        if (paging != null) {
            pageable = PageRequest.of(paging.getPage() - 1, paging.getLimit(), sort);
        }

        /*
        ========================
        Process
        ========================
        */
        Page<Sample> samplePageList = null;
        try {
            List<Sample> sampleList = sampleRepository.executeCustomSelectQuery(parameter, paging, Sample.class);
            System.out.println("sampleList = " + sampleList);

            samplePageList = new PageImpl<Sample>(sampleList, pageable, sampleList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return samplePageList;
    }

    private List<Sample> retrieveAllWithParameter(SampleParameter parameter) {
        List<Sample> sampleList = null;
        try {
            sampleList = sampleRepository.executeCustomSelectQuery(parameter, Sample.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sampleList;
    }

}
