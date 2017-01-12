package bibliotheque;

import java.util.List;

public interface ServiceLoader {
	List<Service> load(Bibliotheque b);
}
