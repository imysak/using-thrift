/**
 * entities 
 */
 
# Ihor Mysak (ihor.mysak@gmail.com)

namespace java com.imysak.thrift.entities
# namespace js com.imysak.thrift.entities



struct IStat {
  1: string name,
  2: i32 totalRequests
}

struct IRequest {
  1: string myName,
  2: string text,
}

struct IResponse {
  1: required bool status,
  2: optional string text
}