variable "host_name" {
  description = "Hostname"
  type        = string
}

variable "name" {
  description = "Name of the sub domain"
  type        = string
}

variable "ip" {
  description = "ip address"
  type        = string
}

variable "type" {
  description = "Type record"
  type        = string
  default     = "A"
}

variable "ttl" {
  description = "Time To Live"
  type        = number
  default     = 1
}

variable "proxied" {
  description = "Proxied"
  type        = bool
  default     = true
}
