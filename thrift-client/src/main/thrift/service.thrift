/**
 * entities 
 */
 
# Ihor Mysak (ihor.mysak@gmail.com)

include "entities.thrift"

namespace java com.imysak.thrift
# namespace js com.imysak.thrift

exception INameMissed {
    1: string message
}

service IService {

   /**
    * test connection
    */
    void ping(),
    
   /**
    * request
    */
    entities.IResponse query(1:entities.IRequest request),

   /**
    * get stat by name
    */
    entities.IStat getStat(1:string name)
            throws (1: INameMissed exc1),

   /**
    * get all stats
    */
    list<entities.IStat> getStats(),

}
