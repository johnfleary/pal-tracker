package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {
    private String port;
    private String mem_lim;
    private String cf_inst_ind;
    private String cf_inst_addr;
    private Map<String,String> env = new HashMap<String,String>();

    public EnvController(@Value("${PORT:NOT SET}") String port, @Value("${MEMORY_LIMIT:NOT SET}") String memory_limit, @Value("${CF_INSTANCE_INDEX:NOT SET}") String cf_inst_index, @Value("${CF_INSTANCE_ADDR:NOT SET}") String cf_inst_addr)
    {
        this.port = port;
        this.mem_lim = memory_limit;
        this.cf_inst_ind = cf_inst_index;
        this.cf_inst_addr = cf_inst_addr;
        env.put("PORT",this.port);
        env.put("MEMORY_LIMIT",this.mem_lim);
        env.put("CF_INSTANCE_INDEX",this.cf_inst_ind);
        env.put("CF_INSTANCE_ADDR",this.cf_inst_addr);
    }

    @GetMapping("/env")
    public Map<String,String> getEnv() {
        return this.env;
    }
}
