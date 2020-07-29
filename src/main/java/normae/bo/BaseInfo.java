package normae.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lishanglei
 * @version v1.0.0
 * @date 2020/7/29
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/7/29              lishanglei      v1.0.0           Created
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInfo {

    private Long id ;

    private String birthday;

    private String cnName;

    private Integer isChinese;

    private String updateTime;

}
