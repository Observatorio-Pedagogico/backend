package com.obervatorio_pedagogico.backend.application.controllers.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obervatorio_pedagogico.backend.application.services.dashboard.DashboardService;
import com.obervatorio_pedagogico.backend.domain.model.dashboard.Dashboard;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.ResponseService;
import com.obervatorio_pedagogico.backend.infrastructure.utils.modelMapper.ModelMapperService;
import com.obervatorio_pedagogico.backend.presentation.dto.dashboard.response.DashboardResponse;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/observatorio-pedagogico/api/dashboard")
@AllArgsConstructor
@CrossOrigin
public class DashboardController {

    private DashboardService dashboardService;

    private ResponseService responseService;

    private ModelMapperService modelMapperService;
    
    @GetMapping("/sexo")
    public ResponseEntity<Response<DashboardResponse>> gerarDashboardSexo() {
        Dashboard dashboard = dashboardService.gerarDashboardSexo();

        return responseService.ok(modelMapperService.convert(dashboard, DashboardResponse.class));
    }
}
